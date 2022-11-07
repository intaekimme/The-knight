package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.end.GameEndDto;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GameEndService {

    private final GameRedisRepository gameRedisRepository;

    @Transactional
    public GameEndDto gameEnd(long gameId) {

        // GameEnd 비즈니스 로직 수행
        // 게임 상태 update
        // - 게임 DB에 결과 저장
        // - 사용자 점수 갱신
        // - ?
        // GameEndDto 채워서 리턴


        return null;
    }

}
