package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.prepare.GamePlayersInfoResponse;
import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import com.a301.theknight.domain.game.dto.prepare.request.GameOrderRequest;
import com.a301.theknight.domain.game.dto.prepare.request.GameWeaponChoiceRequest;
import com.a301.theknight.domain.game.dto.prepare.response.*;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GamePrepareService {

    private final GameRepository gameRepository;
    private final GameRedisRepository redisRepository;

    @Transactional
    public GamePlayersInfoResponse getTeamPlayersInfo(long gameId) {
        GamePlayersInfoDto playersAInfo = getTeamPlayersInfo(gameId, Team.A);
        GamePlayersInfoDto playersBInfo = getTeamPlayersInfo(gameId, Team.B);

        return GamePlayersInfoResponse.builder()
                .playersAInfoDto(playersAInfo)
                .playersBInfoDto(playersBInfo)
                .build();
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
    public GameOrderResponse saveOrder(long gameId, long memberId, Team team, GameOrderRequest orderRequest) {
        InGame inGame = getInGame(gameId);
        if (!orderRequest.validate(inGame.getMaxMemberNum() / 2)) {
            throw new CustomWebSocketException(ORDER_NUMBER_IS_INVALID);
        }
        InGamePlayer inGamePlayer = getInGamePlayer(gameId, memberId);
        if (!team.equals(inGamePlayer.getTeam())) {
            throw new CustomWebSocketException(NOT_MATCH_REQUEST_TEAM);
        }

        GameOrderDto[] orderList = getOrderList(inGame, team);
        int playerOrder = inGamePlayer.getOrder();
        int requestOrder = orderRequest.getOrderNumber();

        checkAlreadySelectedNumber(requestOrder, playerOrder, orderList);

        if (playerOrder == requestOrder) {
            cancelOrder(inGamePlayer, orderList);
        } else {
            saveOrder(inGamePlayer, requestOrder, orderList);
        }

        redisRepository.saveInGame(gameId, inGame);
        redisRepository.saveInGamePlayer(gameId, memberId, inGamePlayer);
        return new GameOrderResponse(orderList);
    }

    @Transactional
    public SelectCompleteDto completeSelect(long gameId, long memberId) {
        InGame inGame = getInGame(gameId);
        InGamePlayer inGamePlayer = getInGamePlayer(gameId, memberId);
        Team myTeam = inGamePlayer.getTeam();

        checkLeaderRequest(inGame, inGamePlayer);
        checkOrderSelect(inGame.getTeamInfoData(myTeam));

        GameWeaponData weaponsData = getWeaponsData(gameId, myTeam);
        Game game = getGame(gameId);
        List<InGamePlayer> teamPlayerList = redisRepository.getTeamPlayerList(gameId, myTeam);

        checkWeaponSelect(teamPlayerList, weaponsData, game);

        inGame.completeSelect(myTeam);
        redisRepository.saveInGame(gameId, inGame);
        redisRepository.deleteGameWeaponData(gameId, myTeam);

        return new SelectCompleteDto(inGame.isAllSelected(), myTeam);
    }

    private void cancelOrder(InGamePlayer inGamePlayer, GameOrderDto[] orderList) {
        int deleteOrder = inGamePlayer.getOrder();
        orderList[deleteOrder - 1] = null;
        inGamePlayer.saveOrder(0);
    }

    private GameOrderDto[] getOrderList(InGame inGame, Team team) {
        return inGame.getTeamInfoData(team).getOrderList();
    }

    private void saveOrder(InGamePlayer inGamePlayer, int orderNumber, GameOrderDto[] orderList) {
        int preOrderNumber = inGamePlayer.getOrder();
        inGamePlayer.saveOrder(orderNumber);

        if (preOrderNumber != 0) {
            orderList[orderNumber - 1] = orderList[preOrderNumber - 1];
            orderList[preOrderNumber - 1] = null;
            return;
        }

        orderList[orderNumber - 1] = GameOrderDto.builder()
                .memberId(inGamePlayer.getMemberId())
                .nickname(inGamePlayer.getNickname())
                .image(inGamePlayer.getImage()).build();
    }

    private GamePlayersInfoDto getTeamPlayersInfo(long gameId, Team team) {
        List<InGamePlayer> playerList = redisRepository.getInGamePlayerList(gameId);

        List<PlayerDataDto> players = playerList.stream()
                .map(inGamePlayer -> PlayerDataDto.toDto(inGamePlayer, team))
                .sorted((o1, o2) -> (int) (o1.getMemberId() - o2.getMemberId()))
                .collect(Collectors.toList());

        return GamePlayersInfoDto.builder()
                .maxMember(players.size())
                .players(players).build();
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
            throw new CustomWebSocketException(CAN_COMPLETE_BY_LEADER);
        }
    }

    private GameWeaponData getWeaponsData(long gameId, Team team) {
        return redisRepository.getGameWeaponData(gameId, team)
                .orElseThrow(() -> new CustomWebSocketException(WEAPON_DATA_IS_NOT_EXIST));
    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private void checkAlreadySelectedNumber(int requestOrder, int playerOrder, GameOrderDto[] orderList) {
        if (playerOrder != requestOrder && orderList[requestOrder - 1] != null) {
            throw new CustomWebSocketException(ALREADY_SELECTED_ORDER_NUMBER);
        }
    }

    private InGamePlayer getInGamePlayer(long gameId, Long memberId) {
        return redisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomRestException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

}
