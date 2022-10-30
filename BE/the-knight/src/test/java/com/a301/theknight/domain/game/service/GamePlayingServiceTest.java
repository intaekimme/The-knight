package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.InGame;
import com.a301.theknight.domain.game.entity.Weapon;
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
@Transactional
@Rollback(value = true)
class GamePlayingServiceTest {

    @Autowired
    RedisTemplate<String, InGame> redisTemplate;

    @BeforeEach
    void clearAndSave() {
//        Long size = redisTemplate.opsForList().size(1L + "");
//        while (size-- > 0) {
//            redisTemplate.opsForList().rightPop(1L + "");
//        }

        Long memberId = 3L;
        InGame inGame1 = InGame.builder()
                .memberId(memberId)
                .nickname("before")
                .isLeader(true)
                .order(1).build();
//        redisTemplate.opsForList().rightPush(memberId+"", inGame1);
        redisTemplate.opsForValue().set(memberId+"", inGame1);
    }

    @Test
    @DisplayName("레디스 리스트 저장 테스트")
    void redisListTest() {
        Long memberId = 1L;

        List<InGame> inGames = getListOps(memberId + "");

        assertEquals(1, inGames.size());
        assertEquals(memberId, inGames.get(0).getMemberId());
    }

    @Test
    @DisplayName("레디스 값 수정 테스트")
    void updateValueTest() {
        Long memberId = 3L;

        InGame beforeInGame = getInGame(memberId);
        assertEquals("before", beforeInGame.getNickname());

        InGame ChangeInGame = InGame.builder()
                .memberId(memberId)
                .nickname("change")
                .order(1).build();
        redisTemplate.opsForValue().set(memberId+"", ChangeInGame);

        InGame inGame = getInGame(memberId);
        assertEquals("change", inGame.getNickname());
    }

    private InGame getInGame(Long memberId) {
        return redisTemplate.opsForValue().get(memberId + "");
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