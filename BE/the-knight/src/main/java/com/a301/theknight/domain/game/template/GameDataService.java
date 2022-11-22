package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.prepare.GamePlayersInfoResponse;
import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import com.a301.theknight.domain.game.dto.prepare.response.GamePlayersInfoDto;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.util.GameLockUtil;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public abstract class GameDataService {

    private GameLockUtil gameLockUtil;
    private GameRedisRepository redisRepository;

    public GameDataService(GameLockUtil gameLockUtil, GameRedisRepository redisRepository) {
        this.gameLockUtil = gameLockUtil;
        this.redisRepository = redisRepository;
    }

    @Transactional
    public void sendScreenData(long gameId, SendMessageService messageService) {
        try {
            gameLockUtil.dataLock(gameId, 4,1);

            makeAndSendData(gameId, messageService);
        } finally {
            gameLockUtil.dataUnLock(gameId);
        }
    }

    protected abstract void makeAndSendData(long gameId, SendMessageService messageService);

    @Transactional
    public void sendPlayersData(long gameId, SendMessageService messageService) {
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

}
