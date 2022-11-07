package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.convert.GameStatusResponse;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.util.GameConvertUtil;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@RequiredArgsConstructor
@Service
public class GameConvertService {
    private final GameRedisRepository gameRedisRepository;
    private final GameConvertUtil gameConvertUtil;

    @Transactional
    public List<String> convertComplete(long gameId, long memberId) {
        try {
            while (!gameRedisRepository.lock(gameId)) {
                Thread.sleep(50);
            }

            InGame inGame = getInGame(gameId);
            inGame.addRequestCount();
            gameRedisRepository.saveInGame(gameId, inGame);

            return inGame.isFullCount() ?
                    gameConvertUtil.getPostfixList(inGame.getGameStatus().name()) : null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            gameRedisRepository.unlock(gameId);
        }
    }

    @Transactional
    public GameStatusResponse getGameStatus(long gameId) {
        InGame inGame = getInGame(gameId);
        inGame.initRequestCount();
        gameRedisRepository.saveInGame(gameId, inGame);

        return new GameStatusResponse(inGame.getGameStatus().name());
    }

    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomException(INGAME_IS_NOT_EXIST));
    }

    private InGamePlayer getInGamePlayer(long gameId, long memberId) {
        return gameRedisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private String lockKeyGen(long gameId) {
        return "game:" + gameId + "_convert_lock";
    }

}
