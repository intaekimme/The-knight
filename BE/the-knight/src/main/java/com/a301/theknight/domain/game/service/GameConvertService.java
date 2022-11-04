package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.convert.GameStatusResponse;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@RequiredArgsConstructor
@Service
public class GameConvertService {
    private final GameRedisRepository gameRedisRepository;

    @Transactional
    public List<String> switchCount(long gameId, long memberId) {
        InGame inGame = getInGame(gameId);

        inGame.addRequestCount();
        if (!inGame.isFullCount()) {
            return null;
        }
        //TODO: 게임 시작
        /*
        * inGame의 State를 key, value는 데이터 보낼 postfix의 List를 저장한 Hashmap이 있음.
        * */
        return new ArrayList<>();
    }

    @Transactional
    public GameStatusResponse getGameStatus(long gameId) {
        InGame inGame = getInGame(gameId);

        return new GameStatusResponse(inGame.getStatus().name());
    }

    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomException(INGAME_IS_NOT_EXIST));
    }

    private InGamePlayer getInGamePlayer(long gameId, long memberId) {
        return gameRedisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomException(INGAME_PLAYER_IS_NOT_EXIST));
    }

}
