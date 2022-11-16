package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.game.factory.GameScreenDataServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GameDataServiceTest {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    GameScreenDataServiceFactory factory;

    @BeforeEach
    void setup() {
    }

    @Test
    @DisplayName("redissonclient 생성자 주입 테스트")
    void test1() {
        assertThat(redissonClient).isNotNull();
        assertThat(factory).isNotNull();
    }
}
/**
 * redissonclient -> super -> 구현체 -> factory
 */