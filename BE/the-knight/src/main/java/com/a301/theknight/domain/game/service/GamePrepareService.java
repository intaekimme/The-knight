package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.prepare.GamePlayersInfoDto;
import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import com.a301.theknight.domain.game.dto.prepare.TeamLeaderDto;
import com.a301.theknight.domain.game.dto.prepare.request.GameOrderRequest;
import com.a301.theknight.domain.game.dto.prepare.request.GameWeaponChoiceRequest;
import com.a301.theknight.domain.game.dto.prepare.response.*;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.GamePlayingErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GamePrepareService {

    private final GameRepository gameRepository;
    private final GameRedisRepository redisRepository;

    @Transactional
    public GamePlayersInfoDto getPlayersInfo(long gameId) {
        List<PlayerDataDto> playerDataDtoListA = getPlayersDataList(gameId);
        List<PlayerDataDto> playerDataDtoListB = new ArrayList<>();
        for (PlayerDataDto playerDataDto : playerDataDtoListA) {
            playerDataDtoListB.add(playerDataDto.copyDto());
        }

        playerDataDtoListA.stream().filter(p -> p.getTeam().equals("B")).forEach(PlayerDataDto::resetWeapons);
        playerDataDtoListB.stream().filter(p -> p.getTeam().equals("A")).forEach(PlayerDataDto::resetWeapons);

        return GamePlayersInfoDto.builder()
                .gamePlayersInfoResponseA(GamePlayersInfoResponse.builder()
                        .maxMember(playerDataDtoListA.size())
                        .players(playerDataDtoListA)
                        .build())
                .gamePlayersInfoResponseB(GamePlayersInfoResponse.builder()
                        .maxMember(playerDataDtoListB.size())
                        .players(playerDataDtoListB)
                        .build())
                .build();
    }

    @Transactional
    public GamePrepareDto prepare(long gameId) {
        Game game = getGame(gameId);
        if (!game.isCanStart()) {
            throw new CustomWebSocketException(CAN_NOT_PLAYING_GAME);
        }
        game.changeStatus(GameStatus.PLAYING);
        List<Player> players = game.getPlayers();

        choiceLeader(players);
        initInGameData(game);
        makeInGamePlayerData(gameId, players);
        makeWeaponsData(game);

        return GamePrepareDto.builder()
                .gameWeaponData(getWeaponsData(gameId, Team.A))
                .gameLeaderDto(getLeadersData(game)).build();
    }

    @Transactional
    public GameWeaponResponse choiceWeapon(long gameId, long memberId, GameWeaponChoiceRequest gameWeaponChoiceRequest) {
        InGamePlayer inGamePlayer = getInGamePlayer(gameId, memberId);
        GameWeaponData weaponsData = getWeaponsData(gameId, inGamePlayer.getTeam());

        if (!weaponsData.canTakeWeapon(gameWeaponChoiceRequest.getWeapon())) {
            throw new CustomWebSocketException(NOT_ENOUGH_WEAPON);
        }
        if (!inGamePlayer.canTakeWeapon()) {
            throw new CustomWebSocketException(SELECT_WEAPON_IS_FULL);
        }

        inGamePlayer.choiceWeapon(gameWeaponChoiceRequest.getWeapon(), weaponsData);
        redisRepository.saveInGamePlayer(gameId, memberId, inGamePlayer);
        redisRepository.saveGameWeaponData(gameId, inGamePlayer.getTeam(), weaponsData);

        return new GameWeaponResponse(inGamePlayer.getTeam(), weaponsData);
    }

    @Transactional
    public GameWeaponResponse cancelWeapon(long gameId, Long memberId, Hand deleteHand) {
        InGamePlayer inGamePlayer = getInGamePlayer(gameId, memberId);
        GameWeaponData weaponsData = getWeaponsData(gameId, inGamePlayer.getTeam());

        inGamePlayer.cancelWeapon(deleteHand, weaponsData);
        redisRepository.saveInGamePlayer(gameId, memberId, inGamePlayer);
        redisRepository.saveGameWeaponData(gameId, inGamePlayer.getTeam(), weaponsData);

        return new GameWeaponResponse(inGamePlayer.getTeam(), weaponsData);
    }

    @Transactional
    public GameOrderResponse choiceOrder(long gameId, long memberId, Team team, GameOrderRequest orderRequest) {
        InGame inGame = getInGame(gameId);
        if (!orderRequest.validate(inGame.getMaxMemberNum() / 2)) {
            throw new CustomWebSocketException(ORDER_NUMBER_IS_INVALID);
        }
        int orderNumber = orderRequest.getOrderNumber();
        InGamePlayer inGamePlayer = getInGamePlayer(gameId, memberId);
        if (!inGamePlayer.getTeam().equals(team)) {
            throw new CustomWebSocketException(NOT_MATCH_REQUEST_TEAM);
        }
        TeamInfoData teamInfoData = Team.A.equals(team)
                ? inGame.getTeamAInfo() : inGame.getTeamBInfo();
        if (alreadySelectedOrderNumber(orderNumber, teamInfoData)) {
            if (inGamePlayer.getOrder() == orderNumber) {
                return null;
            }
            throw new CustomWebSocketException(ALREADY_SELECTED_ORDER_NUMBER);
        }

        inGame.choiceOrder(inGamePlayer, orderNumber, teamInfoData);
        redisRepository.saveInGame(gameId, inGame);
        redisRepository.saveInGamePlayer(gameId, memberId, inGamePlayer);

        return new GameOrderResponse(teamInfoData.getOrderList());
    }

    @Transactional
    public SelectCompleteDto completeSelect(long gameId, long memberId) {
        InGame inGame = getInGame(gameId);
        InGamePlayer inGamePlayer = getInGamePlayer(gameId, memberId);

        checkLeaderRequest(inGame, inGamePlayer);
        checkOrderSelect(inGame.getTeamInfoData(inGamePlayer.getTeam()));

        GameWeaponData weaponsData = getWeaponsData(gameId, inGamePlayer.getTeam());
        Game game = getGame(gameId);
        List<InGamePlayer> teamPlayerList = redisRepository.getTeamPlayerList(gameId, inGamePlayer.getTeam());

        checkWeaponSelect(teamPlayerList, weaponsData, game);

        Team team = inGamePlayer.getTeam();
        inGame.completeSelect(team);
        if (inGame.isAllSelected()) {
            inGame.changeStatus(GameStatus.PREDECESSOR);
        }
        redisRepository.saveInGame(gameId, inGame);
        redisRepository.deleteGameWeaponData(gameId, team);

        return new SelectCompleteDto(inGame.isAllSelected(), team);
    }

    private GameLeaderDto getLeadersData(Game game) {
        Long teamALeaderId = getTeamLeaderId(game, Team.A);
        Long teamBLeaderId = getTeamLeaderId(game, Team.B);

        return GameLeaderDto.builder()
                .teamA(new TeamLeaderDto(teamALeaderId == null ? 0 : teamALeaderId))
                .teamB(new TeamLeaderDto(teamBLeaderId == null ? 0 : teamBLeaderId)).build();
    }

    private void checkWeaponSelect(List<InGamePlayer> teamPlayerList, GameWeaponData weaponsData, Game game) {
        if (weaponsData.notAllSelected()) {
            throw new CustomWebSocketException(CAN_NOT_COMPLETE_WEAPON_SELECT);
        }
        GameWeaponData checkWeaponData = GameWeaponData.toWeaponData(game);
        teamPlayerList.forEach(inGamePlayer -> {
            Weapon leftWeapon = inGamePlayer.getLeftWeapon();
            Weapon rightWeapon = inGamePlayer.getRightWeapon();
            if (leftWeapon == null || rightWeapon == null) {
                throw new CustomWebSocketException(CAN_NOT_COMPLETE_WEAPON_SELECT);
            }
            checkWeaponData.choiceWeapon(leftWeapon);
            checkWeaponData.choiceWeapon(rightWeapon);
        });
        if (checkWeaponData.notAllSelected()) {
            throw new CustomWebSocketException(CAN_NOT_COMPLETE_WEAPON_SELECT);
        }
    }

    private void checkOrderSelect(TeamInfoData teamInfoData) {
        Set<Long> idSet = new HashSet<>();
        for (GameOrderDto gameOrderDto : teamInfoData.getOrderList()) {
            if (gameOrderDto == null || !idSet.add(gameOrderDto.getMemberId())) {
                throw new CustomWebSocketException(CAN_NOT_COMPLETE_ORDER_SELECT);
            }
        }
    }

    private void checkLeaderRequest(InGame inGame, InGamePlayer inGamePlayer) {
        TeamInfoData teamInfoData = inGame.getTeamInfoData(inGamePlayer.getTeam());
        if (teamInfoData.getLeaderId() != inGamePlayer.getMemberId()) {
            throw new CustomWebSocketException(GamePlayingErrorCode.CAN_COMPLETE_BY_LEADER);
        }
    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private boolean alreadySelectedOrderNumber(int orderNumber, TeamInfoData teamInfoData) {
        return teamInfoData.getOrderList()[orderNumber - 1] != null;
    }

    private InGamePlayer getInGamePlayer(long gameId, Long memberId) {
        return redisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private List<PlayerDataDto> getPlayersDataList(long gameId) {
        List<InGamePlayer> playerList = redisRepository.getInGamePlayerList(gameId);

        return playerList.stream()
                .map(inGamePlayer -> PlayerDataDto.toDto(inGamePlayer))
                .collect(Collectors.toList());
    }

    private void choiceLeader(List<Player> players) {
        List<Player> teamA = getTeamPlayerList(players, Team.A);
        List<Player> teamB = getTeamPlayerList(players, Team.B);

        Random random = new Random();
        int teamALeaderIndex = random.nextInt(teamA.size());
        int teamBLeaderIndex = random.nextInt(teamB.size());

        Player teamALeader = teamA.get(teamALeaderIndex);
        Player teamBLeader = teamB.get(teamBLeaderIndex);
        teamALeader.becomeLeader();
        teamBLeader.becomeLeader();
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

    private void initInGameData(Game game) {
        Team firstAttackTeam = getRandomFirstAttackTeam();

        TeamInfoData teamAInfo = makeTeamInfoData(game, getTeamLeaderId(game, Team.A));
        TeamInfoData teamBInfo = makeTeamInfoData(game, getTeamLeaderId(game, Team.B));

        redisRepository.saveInGame(game.getId(), InGame.builder()
                .gameStatus(GameStatus.PREPARE)
                .currentAttackTeam(firstAttackTeam)
                .maxMemberNum(game.getCapacity())
                .teamAInfo(teamAInfo)
                .teamBInfo(teamBInfo)
                .turnData(new TurnData()).build());
    }

    private Team getRandomFirstAttackTeam() {
        return (int) (Math.random() * 10) % 2 == 0 ? Team.A : Team.B;
    }

    private Long getTeamLeaderId(Game game, Team team) {
        return game.getTeamLeader(team)
                .orElseThrow(() -> new CustomWebSocketException(ORDER_NUMBER_IS_INVALID))
                .getMember().getId();
    }

    private TeamInfoData makeTeamInfoData(Game game, Long leaderId) {
        int peopleNum = game.getPlayers().size() / 2;

        return TeamInfoData.builder()
                .currentAttackIndex(peopleNum - 1)
                .orderList(new GameOrderDto[peopleNum])
                .leaderId(leaderId == null ? 0 : leaderId).build();
    }

    private void makeWeaponsData(Game game) {
        GameWeaponData gameWeaponDataA = GameWeaponData.toWeaponData(game);
        GameWeaponData gameWeaponDataB = GameWeaponData.toWeaponData(game);

        redisRepository.saveGameWeaponData(game.getId(), Team.A, gameWeaponDataA);
        redisRepository.saveGameWeaponData(game.getId(), Team.B, gameWeaponDataB);
    }

    private GameWeaponData getWeaponsData(long gameId, Team team) {
        return redisRepository.getGameWeaponData(gameId, team)
                .orElseThrow(() -> new CustomWebSocketException(WEAPON_DATA_IS_NOT_EXIST));
    }

    private List<Player> getTeamPlayerList(List<Player> players, Team team) {
        return players.stream().filter(player -> player.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomRestException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

}
