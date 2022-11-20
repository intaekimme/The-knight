package com.a301.theknight.domain.limit.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.util.GameLockUtil;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.extern.slf4j.Slf4j;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@Slf4j
public abstract class TimeLimitServiceTemplate {

    private GameRedisRepository redisRepository;
    private GameLockUtil gameLockUtil;


    public TimeLimitServiceTemplate(GameRedisRepository redisRepository, GameLockUtil gameLockUtil) {
        this.redisRepository = redisRepository;
        this.gameLockUtil = gameLockUtil;
    }

    public void executeTimeLimit(long gameId, SendMessageService sendMessageService, GameStatus preStatus) {
        InGame preInGame = getInGame(gameId);
        int preTurn = preInGame.getTurnNumber();

        try {
//            Thread.sleep(preStatus.getLimitMilliSeconds());
            int time = (int)preStatus.getLimitMilliSeconds() / 200;
            for (int i = 0; i < time; i++) {
                Thread.sleep(190);
            }
            InGame curInGame = getInGame(gameId);
            if (curInGame.getTurnNumber() != preTurn || !preStatus.equals(curInGame.getGameStatus())) {
                log.info(" [Not TimeOut Logic] {}, nextStatus = {}", preStatus, curInGame.getGameStatus());
                return;
            }

            try {
                gameLockUtil.dataLock(gameId, 1, 6);
                log.info(" [Start Time Out] : status = {}", curInGame.getGameStatus().name());
                sendMessageService.convertCall(gameId);
                runLimitLogic(gameId);
            } finally {
                gameLockUtil.dataUnLock(gameId);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void runLimitLogic(long gameId);

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }
}
