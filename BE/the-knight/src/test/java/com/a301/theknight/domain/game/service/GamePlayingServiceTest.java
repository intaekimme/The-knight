package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.InGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GamePlayingServiceTest {

    @Autowired
    RedisTemplate<String, InGame> redisTemplate;

    @BeforeEach
    void clear() {
        Long size = redisTemplate.opsForList().size(1L + "");
        while (size-- > 0) {
            redisTemplate.opsForList().rightPop(1L + "");
        }
    }

    @Test
    @DisplayName("레디스 리스트 저장 테스트")
    void redisListTest() {
        Long memberId = 1L;
        InGame inGame1 = InGame.builder()
                .memberId(memberId)
                .order(1).build();
        InGame inGame2 = InGame.builder()
                .memberId(memberId)
                .order(2).build();

        redisTemplate.opsForList().rightPush(memberId+"", inGame1);
        redisTemplate.opsForList().rightPush(memberId+"", inGame2);

        List<InGame> inGames = getListOps(memberId + "");
        assertEquals(2, inGames.size());
        assertEquals(inGame1.getMemberId(), inGames.get(0).getMemberId());
    }

    // list (opsForList)
    public void setListOps(String key, List<InGame> values){
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    public List<InGame> getListOps(String key){
        Long len = redisTemplate.opsForList().size(key);
        return len == 0 ? new ArrayList<>() : redisTemplate.opsForList().range(key, 0, len-1);
    }

}