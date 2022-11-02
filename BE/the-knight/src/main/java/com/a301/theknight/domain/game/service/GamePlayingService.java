package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.playing.PlayerStateDto;
import com.a301.theknight.domain.game.dto.playing.TeamLeaderDto;
import com.a301.theknight.domain.game.dto.playing.request.GameOrderRequest;
import com.a301.theknight.domain.game.dto.playing.request.GameWeaponChoiceRequest;
import com.a301.theknight.domain.game.dto.playing.response.*;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.GameWeaponData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TeamInfoData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.GamePlayingErrorCode;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
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
    public void prepareToStartGame(long gameId) {
        Game game = getGame(gameId);
        List<Player> players = game.getPlayers();

        choiceLeader(players);
        makeInGameData(gameId, game);
        makeInGamePlayerData(gameId, players);
        makeWeaponsData(game);
    }

    @Transactional
    public GamePrepareDto gameStart(long gameId) {
        InGame inGame = getInGame(gameId);
        inGame.addRequestCount();
        if (!inGame.allPlayerCanStart()) {
            return null;
        }

        Game game = getGame(gameId);
        GameLeaderDto gameLeaderDto = getLeaders(game);
        GameWeaponData gameWeaponData = getWeaponsData(gameId, Team.A);

        return GamePrepareDto.builder()
                .gameWeaponData(gameWeaponData)
                .gameLeaderDto(gameLeaderDto).build();
    }

    private GameLeaderDto getLeaders(Game game) {
        return GameLeaderDto.builder()
                .teamA(new TeamLeaderDto(getTeamLeaderId(game, Team.A)))
                .teamB(new TeamLeaderDto(getTeamLeaderId(game, Team.B))).build();
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

    @Transactional
    public boolean completeSelect(long gameId, long memberId, Team team) {
        /**
         * 1. ingame에 팀으로 teamData 찾기
         *  -> 팀 리더의 요청인지 확인
         *  -> OrderList에 null이 없고 다 다른사람인지
         * 2. InGamePlayerList 찾기
         *  -> 각각 플레이어가 선택한 무기가 있는지 & 그 무기 카운트 하고 game에 저장된 값과 같은지
         * 3. InGame -> TeamDataInfo에 선택 완료 Flag를 true로 변경
         * 4. 레디스의 GameWeapon 데이터를 삭제
         * 5. InGame에 두 팀 모두 선택 완료 상태인지 확인, 응답의 분기!!
         *  - 한 팀만 선택 : 그 팀한테 선택완료 메시지
         *  - 두 팀 모두 : 다음 화면으로 넘어가라는 응답 (어떤 응답?) + (서버에도 prepare를 위한 메시지??)
         */
        InGame inGame = getInGame(gameId);
        InGamePlayer inGamePlayer = getInGamePlayer(gameId, memberId);

        checkLeaderRequest(inGame, inGamePlayer);
        checkOrderSelect(inGame.getTeamInfoData(inGamePlayer.getTeam()));

        List<InGamePlayer> teamPlayerList = redisRepository.getTeamPlayerList(gameId, inGamePlayer.getTeam());
        Game game = getGame(gameId);
        GameWeaponData weaponsData = getWeaponsData(gameId, inGamePlayer.getTeam());
        checkWeaponSelect(teamPlayerList, weaponsData, game);

        inGame.completeSelect(team);
        redisRepository.saveInGame(gameId, inGame);
        redisRepository.deleteGameWeaponData(gameId, team);

        //TODO: 응답 바꾸기??
        if (inGame.isAllSelected()) {
            //TODO: 선택 완료, 제한 시간 멈추기??
            return true;
        }
        return false;
    }

    private void checkWeaponSelect(List<InGamePlayer> teamPlayerList, GameWeaponData weaponsData, Game game) {
        if (weaponsData.isAllSelected()) {
            throw new CustomException(CAN_NOT_COMPLETE_WEAPON_SELECT);
        }
        GameWeaponData checkWeaponData = GameWeaponData.toWeaponData(game);
        teamPlayerList.forEach(inGamePlayer -> {
            Weapon leftWeapon = inGamePlayer.getLeftWeapon();
            Weapon rightWeapon = inGamePlayer.getRightWeapon();
            if (leftWeapon == null || rightWeapon == null) {
                throw new CustomException(CAN_NOT_COMPLETE_WEAPON_SELECT);
            }
            checkWeaponData.choiceWeapon(leftWeapon);
            checkWeaponData.choiceWeapon(rightWeapon);
        });
        if (checkWeaponData.isAllSelected()) {
            throw new CustomException(CAN_NOT_COMPLETE_WEAPON_SELECT);
        }
    }

    private void checkOrderSelect(TeamInfoData teamInfoData) {
        //다 선택됐는지, 겹치는 멤버 Id는 없는지
        Set<Long> idSet = new HashSet<>();
        for (GameOrderDto gameOrderDto : teamInfoData.getOrderList()) {
            if (gameOrderDto == null || !idSet.add(gameOrderDto.getMemberId())) {
                throw new CustomException(CAN_NOT_COMPLETE_ORDER_SELECT);
            }
        }
    }

    private void checkLeaderRequest(InGame inGame, InGamePlayer inGamePlayer) {
        TeamInfoData teamInfoData = inGame.getTeamInfoData(inGamePlayer.getTeam());
        if (teamInfoData.getLeaderId() != inGamePlayer.getMemberId()) {
            throw new CustomException(GamePlayingErrorCode.CAN_COMPLETE_BY_LEADER);
        }
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
        teamPlayerList.sort(Comparator.comparingInt(InGamePlayer::getOrder));

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

    private void choiceLeader(List<Player> players) {
        List<Player> teamA = getTeamPlayerList(players, Team.A);
        List<Player> teamB = getTeamPlayerList(players, Team.B);

        Random random = new Random();
        int teamALeaderIndex = random.nextInt(teamA.size() - 1);
        int teamBLeaderIndex = random.nextInt(teamB.size() - 1);

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

    private void makeInGameData(long gameId, Game game) {
        Team firstAttackTeam = (int) (Math.random() * 10) % 2 == 0 ? Team.A : Team.B;

        TeamInfoData teamAInfo = makeTeamInfoData(game, getTeamLeaderId(game, Team.A));
        TeamInfoData teamBInfo = makeTeamInfoData(game, getTeamLeaderId(game, Team.B));

        redisRepository.saveInGame(gameId, InGame.builder()
                .currentAttackTeam(firstAttackTeam)
                .teamAInfo(teamAInfo)
                .teamBInfo(teamBInfo).build());
    }

    private Long getTeamLeaderId(Game game, Team team) {
        return game.getTeamLeader(team)
                .orElseThrow(() -> new CustomException(ORDER_NUMBER_IS_INVALID)).getMember().getId();
    }

    private TeamInfoData makeTeamInfoData(Game game, long leaderId) {
        int peopleNum = game.getPlayers().size() / 2;

        return TeamInfoData.builder()
                .currentAttackIndex(0)
                .peopleNum(peopleNum)
                .orderList(new GameOrderDto[peopleNum])
                .leaderId(leaderId).build();
    }

    private void makeWeaponsData(Game game) {
        GameWeaponData gameWeaponDataA = GameWeaponData.toWeaponData(game);
        GameWeaponData gameWeaponDataB = GameWeaponData.toWeaponData(game);

        redisRepository.saveGameWeaponData(game.getId(), Team.A, gameWeaponDataA);
        redisRepository.saveGameWeaponData(game.getId(), Team.B, gameWeaponDataB);
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

    @Transactional
    public GamePreAttackResponse getPreAttack(long gameId) {
        InGame inGame = getInGame(gameId);
        return new GamePreAttackResponse(inGame.getCurrentAttackTeam());
    }
}
