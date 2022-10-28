package com.a301.theknight.domain.player.service;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.dto.PlayerEntryResponse;
import com.a301.theknight.domain.player.dto.PlayerReadyResponse;
import com.a301.theknight.domain.player.dto.PlayerTeamResponse;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.GameWaitingErrorCode;
import com.a301.theknight.global.error.errorcode.MemberErrorCode;
import com.a301.theknight.global.error.errorcode.PlayerErrorCode;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class PlayerWebsocketService {

    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public PlayerEntryResponse entry(long gameId, long memberId){
        Game entryGame = getGame(gameId);
        if(!isWaiting(entryGame)){
            throw new CustomException(GameWaitingErrorCode.GAME_IS_NOT_READY_STATUS);
        }
        if(!isEnterPossible(entryGame)){
            //TODO 커스텀 예외처리로 refactoring
            throw new CustomException(GameWaitingErrorCode.CAN_NOT_ACCOMMODATE);
        }
        Member entryMember = getMember(memberId);
        Player entryPlayer = Player.builder().member(entryMember).game(entryGame).build();

        return PlayerEntryResponse.builder().playerId(entryPlayer.getId())
                .nickname(entryMember.getNickname())
                .image(entryMember.getImage())
                .build();
    }

    @Transactional
    public long exit(long gameId, long memberId){
        Game exitGame = getGame(gameId);

        if(!isWaiting(exitGame)){
            throw new CustomException(GameWaitingErrorCode.GAME_IS_NOT_READY_STATUS);
        }
        Member findMember = getMember(memberId);
        Player exitPlayer = getPlayer(findMember);
        exitPlayer.exitGame();

        long exitPlayerId = exitPlayer.getId();
        playerRepository.delete(exitPlayer);

        return exitPlayerId;
    }

    @Transactional
    public PlayerTeamResponse team(long gameId, long memberId, String team){
        Game findGame = getGame(gameId);
        Member findMember = getMember(memberId);

        Player findPlayer = getPlayer(findGame, findMember);
        //TODO NotNull 유효성 검사
        findPlayer.selectTeam(Team.A.name().equals(team) ? Team.A : Team.B);

        return PlayerTeamResponse.builder()
                .playerId(findPlayer.getId())
                .team(findPlayer.getTeam().name())
                .build();
    }

    @Transactional
    public PlayerReadyResponse ready(long gameId, long memberId, boolean isReady){
        Game findGame = getGame(gameId);
        Member findMember = getMember(memberId);

        Player readyPlayer = getPlayer(findGame, findMember);

        readyPlayer.ready(isReady);

        if(!isOwner(findGame, readyPlayer)){
            return PlayerReadyResponse.builder()
                    .playerId(readyPlayer.getId())
                    .readyStatus(readyPlayer.isReady())
                    .build();
        }else{
            //TODO 방장일 경우 게임 시작 가능 여부 로직 처리
            return null;
        }
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_IS_NOT_EXIST));
    }

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

    private Player getPlayer(Member member){
        //TODO 커스텀 예외처리로 refactoring
        return playerRepository.findByMember(member)
                .orElseThrow(() -> new CustomException(PlayerErrorCode.PLAYER_IS_NOT_EXIST));
    }

    private Player getPlayer(Game game, Member member){
        //TODO 커스텀 예외처리로 refactoring
        return playerRepository.findByGameAndMember(game, member)
                .orElseThrow(() -> new CustomException(PlayerErrorCode.PLAYER_IS_NOT_EXIST));
    }

    private boolean isWaiting(Game game){
        return game.getStatus() == GameStatus.WAITING;
    }
    private boolean isEnterPossible(Game game){
        return game.getCapacity() > game.getPlayers().size();
    }

    private boolean isOwner(Game game, Player player){
        return game.getPlayers().get(0).equals(player);
    }
}
