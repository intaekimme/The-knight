package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.entity.redis.GameWeaponData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.dto.playing.*;
import com.a301.theknight.domain.game.dto.playing.request.GameOrderRequest;
import com.a301.theknight.domain.game.dto.playing.request.GameWeaponChoiceRequest;
import com.a301.theknight.domain.game.dto.playing.response.*;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.redis.TeamInfoData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GamePlayingService {

    private final GameRepository gameRepository;
    private final GameRedisRepository redisRepository;

    @Transactional
    public boolean canStartGame(long gameId, String setGame) {
        Game game = getGame(gameId);
        return game.isCanStart() && game.getSetGame().equals(setGame);
    }

    @Transactional
    public GameMembersInfoDto getMembersInfo(long gameId) {
        Map<String, PlayerStateDto> teamA = getTeamPlayersInfo(gameId, Team.A);
        Map<String, PlayerStateDto> teamB = getTeamPlayersInfo(gameId, Team.B);

        return GameMembersInfoDto.builder()
                .peopleNum(teamA.size())
                .teamA(teamA)
                .teamB(teamB).build();
    }

    @Transactional
    public GamePrepareDto prepareToStartGame(long gameId) {
        Game game = getGame(gameId);
        List<Player> players = game.getPlayers();

        GameLeaderDto gameLeaderDto = choiceLeader(players);
        makeInGameData(gameId, game, gameLeaderDto);
        makeInGamePlayerData(gameId, players);

        GameWeaponData gameWeaponData = makeWeaponsData(game);

        return GamePrepareDto.builder()
                .gameWeaponData(gameWeaponData)
                .gameLeaderDto(gameLeaderDto).build();
    }

    @Transactional
    public GameWeaponResponse choiceWeapon(long gameId, long memberId, GameWeaponChoiceRequest gameWeaponChoiceRequest) {
        InGamePlayer inGamePlayer = getInGamePlayer(gameId, memberId);
        GameWeaponData weaponsData = getWeaponsData(gameId, inGamePlayer.getTeam());

        if (!weaponsData.canTakeWeapon(gameWeaponChoiceRequest.getWeapon())) {
            throw new CustomException(NOT_ENOUGH_WEAPON);
        }
        if (!inGamePlayer.canTakeWeapon()) {
            throw new CustomException(SELECT_WEAPON_IS_FULL);
        }

        inGamePlayer.choiceWeapon(gameWeaponChoiceRequest.getWeapon(), weaponsData);
        redisRepository.saveInGamePlayer(gameId, memberId, inGamePlayer);
        redisRepository.saveGameWeaponData(gameId, inGamePlayer.getTeam(), weaponsData);

        return new GameWeaponResponse(inGamePlayer.getTeam(), weaponsData);
    }

    @Transactional
    public GameWeaponResponse cancelWeapon(long gameId, Long memberId, boolean isLeft) {
        InGamePlayer inGamePlayer = getInGamePlayer(gameId, memberId);
        GameWeaponData weaponsData = getWeaponsData(gameId, inGamePlayer.getTeam());

        inGamePlayer.cancelWeapon(isLeft, weaponsData);
        redisRepository.saveInGamePlayer(gameId, memberId, inGamePlayer);
        redisRepository.saveGameWeaponData(gameId, inGamePlayer.getTeam(), weaponsData);

        return new GameWeaponResponse(inGamePlayer.getTeam(), weaponsData);
    }

    @Transactional
    public GameOrderResponse choiceOrder(long gameId, long memberId, GameOrderRequest orderRequest) {
        InGame inGame = getInGame(gameId);
        if (orderRequest.validate(inGame.getTeamPlayerSize())) {
            throw new CustomException(ORDER_NUMBER_IS_INVALID);
        }
        int orderNumber = orderRequest.getOrderNumber();
        InGamePlayer inGamePlayer = getInGamePlayer(gameId, memberId);

        TeamInfoData teamInfoData = inGamePlayer.getTeam().equals(Team.A)
                ? inGame.getTeamAInfo() : inGame.getTeamBInfo();
        if (alreadySelectedOrderNumber(orderNumber, teamInfoData)) {
            if (inGamePlayer.getOrder() == orderNumber) {
                return null;
            }
            throw new CustomException(ALREADY_SELECTED_ORDER_NUMBER);
        }

        inGame.choiceOrder(inGamePlayer, orderNumber);
        redisRepository.saveInGame(gameId, inGame);
        redisRepository.saveInGamePlayer(gameId, memberId, inGamePlayer);

        return new GameOrderResponse(inGamePlayer.getTeam(), teamInfoData.getOrderList());
    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomException(INGAME_IS_NOT_EXIST));
    }

    private boolean alreadySelectedOrderNumber(int orderNumber, TeamInfoData teamInfoData) {
        return teamInfoData.getOrderList()[orderNumber - 1] != null;
    }

    private InGamePlayer getInGamePlayer(long gameId, Long memberId) {
        return redisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private Map<String, PlayerStateDto> getTeamPlayersInfo(long gameId, Team team) {
        List<InGamePlayer> teamPlayerList = redisRepository.getTeamPlayerList(gameId, team);
        Collections.sort(teamPlayerList, (o1, o2) -> o1.getOrder() - o2.getOrder());

        List<PlayerStateDto> playerStateDtoList = teamPlayerList.stream()
                .map(inGamePlayer -> PlayerStateDto.builder()
                        .memberId(inGamePlayer.getMemberId())
                        .nickname(inGamePlayer.getNickname())
                        .leftCount(inGamePlayer.getLeftCount())
                        .rightCount(inGamePlayer.getRightCount())
                        .weapons(new ArrayList<>(Arrays.asList(inGamePlayer.getLeftWeapon().name(), inGamePlayer.getRightWeapon().name())))
                        .build())
                .collect(Collectors.toList());

        Map<String, PlayerStateDto> teamMap = new HashMap<>();
        int sequenceNum = 1;
        for (PlayerStateDto playerStateDto : playerStateDtoList) {
            teamMap.put("player" + sequenceNum++, playerStateDto);
        }
        return teamMap;
    }

    private GameLeaderDto choiceLeader(List<Player> players) {
        List<Player> teamA = getTeamPlayerList(players, Team.A);
        List<Player> teamB = getTeamPlayerList(players, Team.B);

        Random random = new Random();
        int teamALeaderIndex = random.nextInt(teamA.size() - 1);
        int teamBLeaderIndex = random.nextInt(teamB.size() - 1);

        Player teamALeader = teamA.get(teamALeaderIndex);
        Player teamBLeader = teamB.get(teamBLeaderIndex);
        teamALeader.becomeLeader();
        teamBLeader.becomeLeader();

        return GameLeaderDto.builder()
                .teamA(new TeamLeaderDto(teamALeader.getMember().getId()))
                .teamB(new TeamLeaderDto(teamBLeader.getMember().getId()))
                .build();
    }

    private void makeInGamePlayerData(long gameId, List<Player> players) {
        List<InGamePlayer> inGamePlayers = players.stream()
                .map(player -> InGamePlayer.builder()
                        .memberId(player.getMember().getId())
                        .nickname(player.getMember().getNickname())
                        .image(player.getMember().getImage())
                        .team(player.getTeam())
                        .isLeader(player.isLeader())
                        .leftCount(3)
                        .rightCount(3).build()).collect(Collectors.toList());
        redisRepository.saveInGamePlayerAll(gameId, inGamePlayers);
    }

    private void makeInGameData(long gameId, Game game, GameLeaderDto gameLeaderDto) {
        Team firstAttackTeam = (int) (Math.random() * 10) % 2 == 0 ? Team.A : Team.B;

        TeamInfoData teamAInfo = makeTeamInfoData(game, gameLeaderDto.getTeamA().getMemberId());
        TeamInfoData teamBInfo = makeTeamInfoData(game, gameLeaderDto.getTeamB().getMemberId());

        redisRepository.saveInGame(gameId, InGame.builder()
                .currentAttackTeam(firstAttackTeam)
                .teamAInfo(teamAInfo)
                .teamBInfo(teamBInfo).build());
    }

    private TeamInfoData makeTeamInfoData(Game game, long leaderId) {
        int peopleNum = game.getPlayers().size() / 2;

        return TeamInfoData.builder()
                .currentAttackIndex(0)
                .peopleNum(peopleNum)
                .orderList(new GameOrderDto[peopleNum])
                .leaderId(leaderId).build();
    }

    private GameWeaponData makeWeaponsData(Game game) {
        GameWeaponData gameWeaponDataA = GameWeaponData.toWeaponData(game);
        GameWeaponData gameWeaponDataB = GameWeaponData.toWeaponData(game);

        redisRepository.saveGameWeaponData(game.getId(), Team.A, gameWeaponDataA);
        redisRepository.saveGameWeaponData(game.getId(), Team.B, gameWeaponDataB);
        return gameWeaponDataA;
    }

    private GameWeaponData getWeaponsData(long gameId, Team team) {
        return redisRepository.getGameWeaponData(gameId, team)
                .orElseThrow(() -> new CustomException(WEAPON_DATA_IS_NOT_EXIST));
    }

    private List<Player> getTeamPlayerList(List<Player> players, Team team) {
        return players.stream().filter(player -> player.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(GameErrorCode.GAME_IS_NOT_EXIST));
    }
}
