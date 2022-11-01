package com.a301.theknight.domain.player.repository;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
@DataJpaTest
class PlayerRepositoryTest {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    GameRepository gameRepository;


    static Member testMember;

    @BeforeAll
    static void beforeAll() {
        testMember = Member.builder()
                .nickname("테스트유저1")
                .image("imageURL")
                .email("test@naver.com")
                .password("password")
                .role("ROLE_USER").build();
    }

    @Disabled
    @Test
    @DisplayName("history player 조회 / 10개 테스트")
    void getMemberHistory1() {
        testMember = memberRepository.save(testMember);

        for (int i = 0; i < 12; i++) {
            Game game = Game.builder()
                    .title("game" + i)
                    .capacity(10)
                    .sword(4).twin(1).shield(3).hand(2).build();
            game = gameRepository.save(game);
            Player player = Player.builder().member(testMember).game(game).build();
            if (i % 2 == 0) {
                player.winGame();
            } else {
                player.loseGame();
            }

            playerRepository.save(player);
        }

        List<Player> list = playerRepository.findTenByMemberId(testMember.getId());

        assertEquals(10, list.size());
    }

    @Test
    @DisplayName("이전 플레이 기록 / 정렬 테스트")
    void getMemberHistory2() {
        testMember = memberRepository.save(testMember);

        Game game1 = Game.builder()
                .title("game1")
                .capacity(10)
                .sword(4).twin(1).shield(3).hand(2).build();
        game1.changeStatus(GameStatus.END);
        game1 = gameRepository.save(game1);
        Game game2 = Game.builder()
                .title("game2")
                .capacity(6)
                .sword(3).twin(1).shield(1).hand(1).build();
        game2.changeStatus(GameStatus.END);
        game2 = gameRepository.save(game2);

        Player player1 = Player.builder().member(testMember).game(game1).build();
        Player player2 = Player.builder().member(testMember).game(game2).build();
        playerRepository.save(player1);
        playerRepository.save(player2);
        player1.loseGame();
        player2.winGame();

        List<Player> list = playerRepository.findTenByMemberId(testMember.getId());

        assertEquals(2, list.size());
        assertEquals(game2.getTitle(), list.get(0).getGame().getTitle());
    }
}