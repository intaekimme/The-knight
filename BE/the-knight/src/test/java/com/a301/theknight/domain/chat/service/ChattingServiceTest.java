package com.a301.theknight.domain.chat.service;

import com.a301.theknight.domain.chatting.dto.ChattingRequest;
import com.a301.theknight.domain.chatting.dto.ChattingResponse;
import com.a301.theknight.domain.chatting.entity.Chatting;
import com.a301.theknight.domain.chatting.entity.ChattingSet;
import com.a301.theknight.domain.chatting.repository.ChattingRepository;
import com.a301.theknight.domain.chatting.service.ChattingService;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Disabled
@ExtendWith(MockitoExtension.class)
class ChattingServiceTest {

    @Mock
    ChattingRepository chattingRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    GameRepository gameRepository;

    ChattingService chattingService;

    static Member testMember;
    static Game testGame;
    static Chatting testChatting;
    static ChattingRequest chattingRequest;

    @BeforeAll
    static void beforeAll() {
        testMember = Member.builder()
                .nickname("테스트유저1")
                .image("imageURL")
                .email("test@naver.com")
                .password("password")
                .role("ROLE_USER").build();
        testGame = Game.builder()
                .title("testGame")
                .capacity(10).build();

        chattingRequest = new ChattingRequest();
        chattingRequest.setChattingSet(ChattingSet.A);
        chattingRequest.setContent("content");

        testChatting = Chatting.builder()
                .member(testMember)
                .game(testGame)
                .chattingSet(chattingRequest.getChattingSet())
                .content(chattingRequest.getContent()).build();
    }

    @BeforeEach
    void setUp() {
        chattingService = new ChattingService(chattingRepository, memberRepository, gameRepository);
    }

    @Test
    @DisplayName("채팅 응답 테스트")
    void chattingResponse() {
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));
        given(chattingRepository.save(any())).willReturn(testChatting);

        ChattingResponse chattingResponse = chattingService.makeResponse(1L, 1L, chattingRequest);

        assertEquals(chattingRequest.getChattingSet().name(), chattingResponse.getChattingSet());
        assertEquals(chattingRequest.getContent(), chattingResponse.getContent());
        assertEquals(testMember.getNickname(), chattingResponse.getNickname());
    }

}