package com.a301.theknight.domain.game.repository;

import com.a301.theknight.domain.game.entity.redis.GameWeaponData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.player.entity.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class GameRedisRepository {
    private final RedisTemplate<String, InGame> inGameRedisTemplate; //value로 저장
    private final RedisTemplate<String, InGamePlayer> playerRedisTemplate; //hashset으로 저장
    private final RedisTemplate<String, GameWeaponData> weaponRedisTemplate; //value로 저장
    private final RedisTemplate<String, String> lockRedisTemplate; //value로 저장

    public Optional<InGame> getInGame(long gameId) {
        return Optional.ofNullable(inGameRedisTemplate.opsForValue().get(gameKeyGen(gameId)));
    }

    public Optional<GameWeaponData> getGameWeaponData(long gameId, Team team) {
        return Optional.ofNullable(weaponRedisTemplate.opsForValue().get(weaponKeyGen(gameId, team)));
    }

    public List<InGamePlayer> getInGamePlayerList(long gameId) {
        return playerRedisTemplate.opsForHash().entries(gameKeyGen(gameId))
                .values().stream()
                .map(value -> (InGamePlayer) value).collect(Collectors.toList());
    }

    public List<InGamePlayer> getTeamPlayerList(long gameId, Team team) {
        return playerRedisTemplate.opsForHash().entries(gameKeyGen(gameId))
                .values().stream()
                .map(value -> (InGamePlayer) value)
                .filter(inGamePlayer -> inGamePlayer.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    public Optional<InGamePlayer> getInGamePlayer(long gameId, long memberId) {
        return Optional.ofNullable((InGamePlayer) playerRedisTemplate.opsForHash()
                .get(gameKeyGen(gameId), playerKeyGen(memberId)));
    }

    public InGame saveInGame(long gameId, InGame inGame) {
        inGameRedisTemplate.opsForValue().set(gameKeyGen(gameId), inGame);
        return inGame;
    }

    public InGamePlayer saveInGamePlayer(long gameId, long memberId, InGamePlayer inGamePlayer) {
        playerRedisTemplate.opsForHash().put(gameKeyGen(gameId), playerKeyGen(memberId), inGamePlayer);
        return inGamePlayer;
    }

    public void saveInGamePlayerAll(long gameId, List<InGamePlayer> inGamePlayers) {
        Map<Long, InGamePlayer> map = inGamePlayers.stream()
                .collect(Collectors.toMap(InGamePlayer::getMemberId, inGamePlayer -> inGamePlayer));
        playerRedisTemplate.opsForHash().putAll(gameKeyGen(gameId), map);
    }

    public GameWeaponData saveGameWeaponData(long gameId, Team team, GameWeaponData gameWeaponData) {
        weaponRedisTemplate.opsForValue().set(weaponKeyGen(gameId, team), gameWeaponData);
        return gameWeaponData;
    }

    public Boolean lock(long gameId) {
        String key = lockKeyGen(gameId);

        return lockRedisTemplate.opsForValue()
                .setIfAbsent(key, "lock", Duration.ofMillis(3000L));
    }

    public Boolean unlock(long gameId) {
        return lockRedisTemplate.delete(lockKeyGen(gameId));
    }

    public void deleteGameWeaponData(long gameId, Team team) {
        weaponRedisTemplate.delete(weaponKeyGen(gameId, team));
    }

    private String lockKeyGen(long gameId) {
        return "lock_game:" + gameId;
    }

    private String playerKeyGen(long memberId) {
        return "member:" + memberId;
    }

    private String gameKeyGen(long gameId) {
        return "game:" + gameId;
    }

    private String weaponKeyGen(long gameId, Team team) {
        return "weapon" + team.name() + ":" + gameId;
    }

}
