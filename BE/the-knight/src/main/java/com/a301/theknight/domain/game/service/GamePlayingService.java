package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.InGame;
import com.a301.theknight.domain.game.dto.playing.*;
import com.a301.theknight.domain.game.entity.Game;
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

    private final RedisTemplate<String, InGame> gameRedisTemplate;
    private final RedisTemplate<String, GameWeaponDto> weaponRedisTemplate;

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
        saveInGameData(gameId, players);

        GameWeaponDto gameWeaponDto = getWeaponsData(game);

        return GamePrepareDto.builder()
                .gameWeaponDto(gameWeaponDto)
                .gameLeaderDto(gameLeaderDto)
                .build();
    }

//    @Transactional
//    public GameWeaponDto choiceWeapon(long gameId, Long memberId, GameWeaponRequest gameWeaponRequest) {
//        InGame inGame = getInGameData(gameId, memberId);
//
//    }

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

    private void saveInGameData(long gameId, List<Player> players) {
        Map<String, InGame> inGameMap = new HashMap<>();

        players.stream().forEach(player -> {
            String inGameKey = inGameKeyGen(player.getMember().getId());

            inGameMap.put(inGameKey, InGame.builder()
                    .memberId(player.getMember().getId())
                    .nickname(player.getMember().getNickname())
                    .team(player.getTeam())
                    .isLeader(player.isLeader())
                    .leftCount(3)
                    .rightCount(3).build());
        });
        String gameKey = gameKeyGen(gameId);
        gameRedisTemplate.opsForHash().putAll(gameKey, inGameMap);
    }

    private GameWeaponDto getWeaponsData(Game game) {
        GameWeaponDto gameWeaponDto = GameWeaponDto.toDto(game);
        weaponRedisTemplate.opsForValue().set(weaponKeyGen(game.getId()), gameWeaponDto);

        return gameWeaponDto;
    }

    private List<Player> getTeamPlayerList(List<Player> players, Team team) {
        return players.stream().filter(player -> player.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    public List<InGame> getInGamePlayerList(String gameKey){
        return gameRedisTemplate.opsForHash().size(gameKey) == 0
                ? new ArrayList<>() : gameRedisTemplate.opsForHash().entries(gameKey)
                    .entrySet().stream().map(Map.Entry::getValue)
                    .map(value -> (InGame) value).collect(Collectors.toList());
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

    private String weaponKeyGen(long gameId) {
        return "weapon:" + gameId;
    }

}
