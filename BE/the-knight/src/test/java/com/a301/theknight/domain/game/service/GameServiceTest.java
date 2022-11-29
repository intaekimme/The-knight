package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.waiting.response.GameInfoResponse;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    GameRepository gameRepository;
    @Mock
    PlayerRepository playerRepository;

    GameService gameService;


    private Member[] testMembers;
    private Game testGame;

    @BeforeEach
    void setUp() {
        testMembers = new Member[4];

        for(int i=0; i<4; i++){
            testMembers[i] = Member.builder()
                    .nickname("testPlayer" + i)
                    .password("testPlayer" + i)
                    .image("testPlayer" + i)
                    .role("ROLE_USER").build();
        }

        testGame = Game.builder()
                .title("testGame")
                .sword(4)
                .twin(3)
                .shield(2)
                .hand(1)
                .capacity(10).build();
//        Player owner = new Player(1L, testMembers[0], testGame);
        Player owner = Player.builder()
                .member(testMembers[0])
                .game(testGame).build();
        owner.setOwner();

        gameService = new GameService(gameRepository,memberRepository,playerRepository);
    }

    @DisplayName("게임방 생성")
    @Test
    void createGame() {
        // given
        // when
        // then
    }


    @Test
    void getGameList() {
    }

    @DisplayName("게임 상세 정보 조회")
    @Test
    void testMethodNameHere() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMembers[0]));
        given(gameRepository.findById(1L)).willReturn(Optional.of(testGame));
        // when
        GameInfoResponse gameInfoResponse = gameService.getGameInfo(1L, 1L);
        // then
        assertEquals(1L, gameInfoResponse.getOwnerId());
    }
}