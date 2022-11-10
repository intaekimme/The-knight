package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.repository.GameRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameAttackDefenseServiceTest {

    @Mock
    GameRedisRepository gameRedisRepository;

    @Mock
    GameAttackDefenseService gameAttackDefenseService;

    @BeforeEach
    void setup() {

    }


    @Test
    void getAttacker() {
    }

    @Test
    void getPreAttack() {
    }

    @Test
    void attack() {

    }

    @Test
    void getAttackInfo() {
    }

    @Test
    void isAttackPass() {
    }

    @Test
    void defense() {
    }

    @Test
    void getDefenseInfo() {
    }

    @Test
    void isDefensePass() {
    }
}