package com.a301.theknight.domain.player.service;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.game.util.GameConvertUtil;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.dto.ReadyDto;
import com.a301.theknight.domain.player.dto.request.PlayerReadyRequest;
import com.a301.theknight.domain.player.dto.request.PlayerTeamRequest;
import com.a301.theknight.domain.player.dto.response.PlayerEntryResponse;
import com.a301.theknight.domain.player.dto.response.PlayerExitDto;
import com.a301.theknight.domain.player.dto.response.PlayerExitResponse;
import com.a301.theknight.domain.player.dto.response.PlayerTeamResponse;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.MemberErrorCode;
import com.a301.theknight.global.error.errorcode.PlayerErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

import static com.a301.theknight.global.error.errorcode.GameWaitingErrorCode.*;

@RequiredArgsConstructor
@Service
public class PlayerService {

    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GameRedisRepository redisRepository;
    private final GameConvertUtil gameConvertUtil;

    @Transactional
    public PlayerEntryResponse entry(long gameId, long memberId){
        Game entryGame = getGameFetchJoin(gameId);
        if(!isWaiting(entryGame)){
            throw new CustomWebSocketException(GAME_IS_NOT_READY_STATUS);
        }
        if(!isEnterPossible(entryGame)){
            throw new CustomWebSocketException(CAN_NOT_ACCOMMODATE);
        }
        checkAlreadyEntry(memberId, entryGame);

        Member entryMember = getMember(memberId);
        Member owner = entryGame.getOwner().getMember();

        if (!owner.getId().equals(entryMember.getId())) {
            playerRepository.save(Player.builder()
                    .member(entryMember)
                    .game(entryGame).build());
        }

        return PlayerEntryResponse.builder()
                .memberId(entryMember.getId())
                .nickname(entryMember.getNickname())
                .image(entryMember.getImage()).build();
    }

    private void checkAlreadyEntry(long memberId, Game entryGame) {
        entryGame.getPlayers().forEach(player -> {
            if (!player.isOwner() && player.getMember().getId().equals(memberId)) {
                throw new CustomWebSocketException(PLAYER_IS_ALREADY_ENTRY);
            }
        });
    }

    @Transactional
    public PlayerExitDto exit(long gameId, long memberId){
        Game findGame = getGame(gameId);

        if(!isWaiting(findGame)){
            throw new CustomWebSocketException(GAME_IS_NOT_READY_STATUS);
        }
        Member findMember = getMember(memberId);
        Player exitPlayer = getPlayer(findGame, findMember);
        exitPlayer.exitGame();

        PlayerExitDto exitDto = new PlayerExitDto(exitPlayer.isOwner(), PlayerExitResponse.builder()
                .memberId(exitPlayer.getMember().getId())
                .nickname(exitPlayer.getMember().getNickname()).build());
        playerRepository.delete(exitPlayer);

        return exitDto;
    }

    @Transactional
    public PlayerTeamResponse team(long gameId, long memberId, PlayerTeamRequest playerTeamMessage){
        Game findGame = getGame(gameId);
        Member findMember = getMember(memberId);

        Player findPlayer = getPlayer(findGame, findMember);
        findPlayer.selectTeam(playerTeamMessage.getTeam());

        return PlayerTeamResponse.builder()
                .memberId(findPlayer.getMember().getId())
                .team(findPlayer.getTeam().name())
                .build();
    }

    @Transactional
    public ReadyDto ready(long gameId, long memberId, PlayerReadyRequest playerReadyMessage){
        Game findGame = getGame(gameId);
        Member findMember = getMember(memberId);

        Player readyPlayer = getPlayer(findGame, findMember);
        readyPlayer.ready(playerReadyMessage.isReadyStatus());

        if(isOwner(findGame, readyPlayer)){
            if (!isEqualPlayerNum(findGame)) {
                throw new CustomWebSocketException(NUMBER_OF_PLAYERS_ON_BOTH_TEAM_IS_DIFFERENT);
            }
            if (!isAllReady(findGame)) {
                throw new CustomWebSocketException(NOT_All_USERS_ARE_READY);
            }
            findGame.changeStatus(GameStatus.PLAYING);
            //TODO: 방장 레디 -> 게임 시작 부분도 Screen-Data 시퀀스로 맞춰서 의존성 분리시키기
            gameConvertUtil.initRequestQueue(gameId, findGame.getCapacity());
            redisRepository.saveInGame(findGame.getId(), makeInGame(findGame));
        }

        return ReadyDto.builder()
                .memberId(readyPlayer.getMember().getId())
                .readyStatus(readyPlayer.isReady())
                .canStart(findGame.isCanStart())
                .build();
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomRestException(MemberErrorCode.MEMBER_IS_NOT_EXIST));
    }

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomRestException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

    private Game getGameFetchJoin(long gameId) {
        return gameRepository.findByIdFetchJoin(gameId)
                .orElseThrow(() -> new CustomRestException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

    private Player getPlayer(Game game, Member member){
        return playerRepository.findByGameAndMember(game, member)
                .orElseThrow(() -> new CustomRestException(PlayerErrorCode.PLAYER_IS_NOT_EXIST));
    }

    private boolean isWaiting(Game game){
        return game.getStatus() == GameStatus.WAITING;
    }
    private boolean isEnterPossible(Game game){
        return game.getCapacity() > game.getPlayers().size();
    }

    private boolean isOwner(Game game, Player player){
        return game.getOwner().getId().equals(player.getId());
    }

    private boolean isEqualPlayerNum(Game game){
        AtomicInteger teamA = new AtomicInteger();
        AtomicInteger teamB = new AtomicInteger();
        game.getPlayers().forEach(player -> {
            AtomicInteger addInteger = Team.A.equals(player.getTeam()) ? teamA : teamB;
            addInteger.getAndIncrement();
        });
        return teamA.intValue() == teamB.intValue();
    }

    private boolean isAllReady(Game game){
        boolean canStart = game.getPlayers().stream()
                .filter(Player::isReady).count() == game.getCapacity();
        if(canStart) {
            game.completeReady();
        }
        return canStart;
    }

    private InGame makeInGame(Game game) {
        InGame initInGame = InGame.builder()
                .gameStatus(GameStatus.PREPARE)
                .maxMemberNum(game.getCapacity())
                .teamAInfo(TeamInfoData.builder().build())
                .teamBInfo(TeamInfoData.builder().build())
                .turnData(makeTurnData()).build();

        return redisRepository.saveInGame(game.getId(), initInGame);
    }

    private TurnData makeTurnData() {
        TurnData turnData = new TurnData();
        turnData.setAttackData(AttackData.builder().build());
        turnData.setDefenseData(DefendData.builder().build());
        turnData.setDoubtData(DoubtData.builder().build());

        return turnData;
    }
}
