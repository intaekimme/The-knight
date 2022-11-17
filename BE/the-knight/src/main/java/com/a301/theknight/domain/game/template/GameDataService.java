package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.prepare.GamePlayersInfoResponse;
import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import com.a301.theknight.domain.game.dto.prepare.response.GamePlayersInfoDto;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public abstract class GameDataService {

    private RedissonClient redissonClient;
    private GameRedisRepository redisRepository;

    public GameDataService(RedissonClient redissonClient, GameRedisRepository redisRepository) {
        this.redissonClient = redissonClient;
        this.redisRepository = redisRepository;
    }

    @Transactional
    public void sendScreenData(long gameId, SendMessageService messageService) {
        RLock dataLock = redissonClient.getLock(dataLockKeyGen(gameId));
        try {
            tryDataLock(dataLock);

            makeAndSendData(gameId, messageService);
            sendPlayersData(gameId, messageService);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unLock(dataLock);
        }
    }

    protected abstract void makeAndSendData(long gameId, SendMessageService messageService);

    private void sendPlayersData(long gameId, SendMessageService messageService) {
        GamePlayersInfoResponse playersInfo = getPlayersInfo(gameId);
        messageService.sendData(gameId, "/a/players", playersInfo.getPlayersAInfoDto());
        messageService.sendData(gameId, "/b/players", playersInfo.getPlayersBInfoDto());
    }

    private GamePlayersInfoResponse getPlayersInfo(long gameId) {
        GamePlayersInfoDto playersAInfo = getTeamPlayersInfo(gameId, Team.A);
        GamePlayersInfoDto playersBInfo = getTeamPlayersInfo(gameId, Team.B);

        return GamePlayersInfoResponse.builder()
                .playersAInfoDto(playersAInfo)
                .playersBInfoDto(playersBInfo)
                .build();
    }

    private GamePlayersInfoDto getTeamPlayersInfo(long gameId, Team team) {
        List<InGamePlayer> playerList = redisRepository.getInGamePlayerList(gameId);

        List<PlayerDataDto> players = playerList.stream()
                .map(inGamePlayer -> PlayerDataDto.toDto(inGamePlayer, team))
                .sorted(Comparator.comparingInt(PlayerDataDto::getOrder))
                .collect(Collectors.toList());

        return GamePlayersInfoDto.builder()
                .maxMember(players.size())
                .players(players).build();
    }

    private void unLock(RLock dataLock) {
        if (dataLock != null && dataLock.isLocked()) {
            dataLock.unlock();
        }
    }

    private void tryDataLock(RLock dataLock) throws InterruptedException {
        if (!dataLock.tryLock(7, 10, TimeUnit.SECONDS)) {
            throw new CustomWebSocketException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
        }
    }

    private String dataLockKeyGen(long gameId) {
        return "data_lock:" + gameId;
    }
}
