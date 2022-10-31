package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.InGame;
import com.a301.theknight.domain.game.dto.playing.*;
import com.a301.theknight.domain.game.dto.playing.request.GameOrderRequest;
import com.a301.theknight.domain.game.dto.playing.request.GameWeaponChoiceRequest;
import com.a301.theknight.domain.game.dto.playing.response.*;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.GamePlayingErrorCode;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;
import java.util.stream.Collectors;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GamePlayingService {

    private final GameRepository gameRepository;

    private final RedisTemplate<String, InGame> gameRedisTemplate;
    private final RedisTemplate<String, GameWeaponData> weaponRedisTemplate;

    @Transactional
    public boolean canStartGame(long gameId, String setGame) {
        return getGame(gameId).isCanStart();
    }

    @Transactional
    public GameMembersInfoDto getMembersInfo(long gameId) {
        List<InGame> playerDataList = getInGamePlayerList(gameKeyGen(gameId));
        int peopleNum = playerDataList.size() / 2;

        Map<String, PlayerStateDto> teamA = getTeamPlayersInfo(playerDataList, Team.A);
        Map<String, PlayerStateDto> teamB = getTeamPlayersInfo(playerDataList, Team.B);

        return GameMembersInfoDto.builder()
                .peopleNum(peopleNum)
                .teamA(teamA)
                .teamB(teamB).build();
    }

    /**
     * - 제한시간 100초 Start → 구독 큐에 넘겨주기
 *     - 캐싱해서 사용, 무기 다 정하면 데이터 삭제
     */

    @Transactional
    public GamePrepareDto prepareToStartGame(long gameId) {
        Game game = getGame(gameId);
        List<Player> players = game.getPlayers();

        GameLeaderDto gameLeaderDto = choiceLeader(players);
        makeInGameData(gameId, players);

        GameWeaponData gameWeaponData = makeWeaponsData(game);

        return GamePrepareDto.builder()
                .gameWeaponData(gameWeaponData)
                .gameLeaderDto(gameLeaderDto)
                .build();
    }

    @Transactional
    public GameWeaponResponse choiceWeapon(long gameId, Long memberId, GameWeaponChoiceRequest gameWeaponChoiceRequest) {
        InGame inGame = getInGameData(gameId, memberId);
        GameWeaponData weaponsData = getWeaponsData(gameId, inGame.getTeam());

        if (!weaponsData.canTakeWeapon(gameWeaponChoiceRequest.getWeapon())) {
            throw new CustomException(NOT_ENOUGH_WEAPON);
        }
        if (!inGame.canTakeWeapon()) {
            throw new CustomException(SELECT_WEAPON_IS_FULL);
        }

        inGame.choiceWeapon(gameWeaponChoiceRequest.getWeapon(), weaponsData);
        saveInGame(gameId, memberId, inGame);
        saveWeaponsData(gameId, inGame.getTeam(), weaponsData);

        return new GameWeaponResponse(inGame.getTeam(), weaponsData);
    }

    @Transactional
    public GameWeaponResponse deleteWeapon(long gameId, Long memberId, boolean isLeft) {
        InGame inGame = getInGameData(gameId, memberId);
        GameWeaponData weaponsData = getWeaponsData(gameId, inGame.getTeam());

        inGame.deleteWeapon(isLeft, weaponsData);
        saveInGame(gameId, memberId, inGame);
        saveWeaponsData(gameId, inGame.getTeam(), weaponsData);

        return new GameWeaponResponse(inGame.getTeam(), weaponsData);
    }

    @Transactional
    public GameOrderResponse choiceOrder(long gameId, long memberId, GameOrderRequest orderRequest) {
        if (orderRequest.validate(getPlayerSize(gameKeyGen(gameId)))) {
            throw new CustomException(ORDER_NUMBER_IS_INVALID);
        }
        List<InGame> inGamePlayerList = getInGamePlayerList(gameKeyGen(gameId));
        int numPeople = inGamePlayerList.size() / 2;

        InGame findInGame = getFindInGameInPlayerList(memberId, inGamePlayerList);
        if (alreadySelectedOrderNumber(orderRequest.getOrderNumber(), findInGame.getTeam(), inGamePlayerList)) {
            if (findInGame.getMemberId() == orderRequest.getOrderNumber()) {
                return null;
            }
            throw new CustomException(ALREADY_SELECTED_ORDER_NUMBER);
        }
        findInGame.saveOrder(orderRequest.getOrderNumber());
        saveInGame(gameId, memberId, findInGame);

        GameOrderDto[] gameOrderDtos = new GameOrderDto[numPeople];
        inGamePlayerList.stream()
                .filter(inGame -> inGame.getTeam().equals(findInGame.getTeam()) && inGame.getOrder() > 0)
                .forEach(inGame -> {
                    int index = inGame.getOrder() - 1;
                    gameOrderDtos[index] = GameOrderDto.builder()
                        .memberId(inGame.getMemberId())
                        .nickname(inGame.getNickname())
                        .image(inGame.getImage()).build();
                });
        return new GameOrderResponse(findInGame.getTeam(), gameOrderDtos);
    }

    private boolean alreadySelectedOrderNumber(int orderNumber, Team team, List<InGame> inGamePlayerList) {
        return inGamePlayerList.stream()
                .filter(inGame -> inGame.getTeam().equals(team) && inGame.getOrder() == orderNumber)
                .collect(Collectors.toList()).size() > 0;
    }

    private InGame getFindInGameInPlayerList(long memberId, List<InGame> inGamePlayerList) {
        return inGamePlayerList.stream().filter(inGame -> inGame.getMemberId().equals(memberId))
                .findFirst().orElseThrow(() -> new CustomException(INGAME_IS_NOT_EXIST));
    }

    private void saveWeaponsData(long gameId, Team team, GameWeaponData weaponsData) {
        weaponRedisTemplate.opsForValue().set(weaponKeyGen(gameId, team), weaponsData);
    }

    private void saveInGame(long gameId, Long memberId, InGame inGame) {
        gameRedisTemplate.opsForHash().put(gameKeyGen(gameId), inGameKeyGen(memberId), inGame);
    }

    private InGame getInGameData(long gameId, Long memberId) {
        Object inGame = gameRedisTemplate.opsForHash().get(gameKeyGen(gameId), inGameKeyGen(memberId));
        if (inGame == null) {
            throw new CustomException(INGAME_IS_NOT_EXIST);
        }
        return (InGame) inGame;
    }

    private Map<String, PlayerStateDto> getTeamPlayersInfo(List<InGame> playerDataList, Team team) {
        Collections.sort(playerDataList, (o1, o2) -> o1.getOrder() - o2.getOrder());
        List<PlayerStateDto> playerStateDtoList = playerDataList.stream()
                .filter(inGame -> team.equals(inGame.getTeam()))
                .map(inGame -> PlayerStateDto.builder()
                        .memberId(inGame.getMemberId())
                        .nickname(inGame.getNickname())
                        .leftCount(inGame.getLeftCount())
                        .rightCount(inGame.getRightCount())
                        .weapons(new ArrayList<>(Arrays.asList(inGame.getLeftWeapon().name(), inGame.getRightWeapon().name())))
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

    private void makeInGameData(long gameId, List<Player> players) {
        Map<String, InGame> inGameMap = new HashMap<>();

        players.stream().forEach(player -> {
            String inGameKey = inGameKeyGen(player.getMember().getId());

            inGameMap.put(inGameKey, InGame.builder()
                    .memberId(player.getMember().getId())
                    .nickname(player.getMember().getNickname())
                    .image(player.getMember().getImage())
                    .team(player.getTeam())
                    .isLeader(player.isLeader())
                    .leftCount(3)
                    .rightCount(3).build());
        });
        String gameKey = gameKeyGen(gameId);
        gameRedisTemplate.opsForHash().putAll(gameKey, inGameMap);
    }

    private GameWeaponData makeWeaponsData(Game game) {
        GameWeaponData gameWeaponDataA = GameWeaponData.toDto(game);
        GameWeaponData gameWeaponDataB = GameWeaponData.toDto(game);

        weaponRedisTemplate.opsForValue().set(weaponKeyGen(game.getId(), Team.A), gameWeaponDataA);
        weaponRedisTemplate.opsForValue().set(weaponKeyGen(game.getId(), Team.B), gameWeaponDataB);
        return gameWeaponDataA;
    }

    private GameWeaponData getWeaponsData(long gameId, Team team) {
        return weaponRedisTemplate.opsForValue().get(weaponKeyGen(gameId, team));
    }

    private List<Player> getTeamPlayerList(List<Player> players, Team team) {
        return players.stream().filter(player -> player.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    public List<InGame> getInGamePlayerList(String gameKey){
        return getPlayerSize(gameKey) == 0
                ? new ArrayList<>() : gameRedisTemplate.opsForHash().entries(gameKey)
                    .entrySet().stream().map(Map.Entry::getValue)
                    .map(value -> (InGame) value).collect(Collectors.toList());
    }

    private Long getPlayerSize(String gameKey) {
        return gameRedisTemplate.opsForHash().size(gameKey);
    }

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

    private String inGameKeyGen(long memberId) {
        return "member:" + memberId;
    }

    private String gameKeyGen(long gameId) {
        return "game:" + gameId;
    }

    private String weaponKeyGen(long gameId, Team team) {
        return "weapon" + team.name() + ":" + gameId;
    }

}
