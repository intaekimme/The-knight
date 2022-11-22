package com.a301.theknight.domain.game.repository;

import com.a301.theknight.domain.game.entity.redis.GameWeaponData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.player.entity.Team;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class GameRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<InGame> getInGame(long gameId) {
        String gameKey = generateGameKey(gameId);
        return getData(gameKey, InGame.class);
    }

    public Optional<GameWeaponData> getGameWeaponData(long gameId, Team team) {
        String weaponKey = generateWeaponKey(gameId, team);
        return getData(weaponKey, GameWeaponData.class);
    }

    public Optional<InGamePlayer> getInGamePlayer(long gameId, long memberId) {
        String playerGameKey = generatePlayerGameKey(gameId);
        String playerKey = generatePlayerKey(memberId);

        return getHashData(playerGameKey, playerKey, InGamePlayer.class);
    }

    public List<InGamePlayer> getInGamePlayerList(long gameId) {
        String playerGameKey = generatePlayerGameKey(gameId);

        return getHashList(playerGameKey, InGamePlayer.class);
    }

    public List<InGamePlayer> getTeamPlayerList(long gameId, Team team) {
        return getInGamePlayerList(gameId).stream()
                .filter(inGamePlayer -> inGamePlayer.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    public InGame saveInGame(long gameId, InGame inGame) {
        String gameKey = generateGameKey(gameId);
        try {
            String jsonData = objectMapper.writeValueAsString(inGame);
            saveData(gameKey, jsonData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return inGame;
    }

    public InGamePlayer saveInGamePlayer(long gameId, long memberId, InGamePlayer inGamePlayer) {
        String gameKey = generatePlayerGameKey(gameId);
        String playerKey = generatePlayerKey(memberId);
        try {
            String jsonData = objectMapper.writeValueAsString(inGamePlayer);
            saveHashData(gameKey, playerKey, jsonData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return inGamePlayer;
    }

    public void saveInGamePlayerAll(long gameId, List<InGamePlayer> inGamePlayers) {
        String gameKey = generatePlayerGameKey(gameId);
        Map<String, String> map = inGamePlayers.stream().collect(Collectors
                        .toMap(inGamePlayer -> generatePlayerKey(inGamePlayer.getMemberId()),
                                inGamePlayer -> {
                                    try {
                                        return objectMapper.writeValueAsString(inGamePlayer);
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    }
                        }));
        saveAllHashData(gameKey, map);
    }

    public GameWeaponData saveGameWeaponData(long gameId, Team team, GameWeaponData gameWeaponData) {
        String weaponKey = generateWeaponKey(gameId, team);
        try {
            String jsonData = objectMapper.writeValueAsString(gameWeaponData);
            saveData(weaponKey, jsonData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return gameWeaponData;
    }

    public void deleteGameWeaponData(long gameId, Team team) {
        redisTemplate.delete(generateWeaponKey(gameId, team));
    }

    public void deleteInGame(long gameId) {
        redisTemplate.delete(generateGameKey(gameId));
    }

    private <T> Optional<T> getData(String key, Class<T> classType) {
        String jsonData = (String) redisTemplate.opsForValue().get(key);

        try {
            if (StringUtils.hasText(jsonData)) {
                return Optional.ofNullable(objectMapper.readValue(jsonData, classType));
            }
            return Optional.empty();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> Optional<T> getHashData(String key, String hashKey, Class<T> classType) {
        String jsonData = (String) redisTemplate.opsForHash().get(key, hashKey);

        try {
            if (StringUtils.hasText(jsonData)) {
                return Optional.ofNullable(objectMapper.readValue(jsonData, classType));
            }
            return Optional.empty();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> getHashList(String key, Class<T> classType) {
        return redisTemplate.opsForHash().entries(key).values().stream()
                .map(object -> (String) object)
                .map(jsonData -> {
                    try {
                        return objectMapper.readValue(jsonData, classType);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }})
                .collect(Collectors.toList());
    }

    private void saveData(String key, String jsonData) {
        redisTemplate.opsForValue().set(key, jsonData);
        redisTemplate.expire(key, Duration.ofMinutes(20L));
    }

    private void saveHashData(String key, String hashKey, String jsonData) {
        redisTemplate.opsForHash().delete(key, hashKey);
        redisTemplate.opsForHash().put(key, hashKey, jsonData);
        redisTemplate.expire(key, Duration.ofMinutes(20L));
    }

    private <T> void saveAllHashData(String key, Map<String, String> dataMap) {
        redisTemplate.opsForHash().putAll(key, dataMap);
        redisTemplate.expire(key, Duration.ofMinutes(20L));
    }

    private String generatePlayerKey(long memberId) {
        return "member:" + memberId;
    }

    private String generatePlayerGameKey(long gameId) {
        return "game_player:" + gameId;
    }

    private String generateGameKey(long gameId) {
        return "game:" + gameId;
    }

    private String generateWeaponKey(long gameId, Team team) {
        return "weapon" + team.name() + ":" + gameId;
    }
}
