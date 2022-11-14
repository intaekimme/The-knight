package com.a301.theknight.domain.game.factory;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.template.GameDataService;
import com.a301.theknight.domain.game.template.PredecessorDataService;
import com.a301.theknight.domain.game.template.PrepareDataService;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@RequiredArgsConstructor
@Component
public class GameDataTemplateFactory {
    private final GameRedisRepository gameRedisRepository;

    private final PrepareDataService prepareDataTemplate;
    private final PredecessorDataService predecessorDataService;

    public GameDataService getGameDataTemplate(long gameId) {
        GameStatus gameStatus = getGameStatus(gameId);

        switch (gameStatus) {
            case PREPARE:
                return prepareDataTemplate;
            case PREDECESSOR:
                return predecessorDataService;
        }
        return null;
    }

    private GameStatus getGameStatus(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST))
                .getGameStatus();
    }
}
