package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    GameService gameService;

    @BeforeEach
    void setUp() {
        // 유저 생성
        for(int i=1; i<=10; i++){
            Member member = Member.builder()
                    .email("player" + Integer.toString(i) + "@email.com")
                    .password("player" + Integer.toString(i))
                    .nickname("player" + Integer.toString(i))
                    .image("image" + Integer.toString(i))
                    .build();

            memberRepository.save(member);
        }

        //  게임방 생성
//        gameService.createGame();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getGameList() {
    }
}