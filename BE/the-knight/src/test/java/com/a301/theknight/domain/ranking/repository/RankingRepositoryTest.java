package com.a301.theknight.domain.ranking.repository;

import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.ranking.entity.Ranking;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest
class RankingRepositoryTest {

    @Autowired
    RankingRepository rankingRepository;

    @Autowired
    MemberRepository memberRepository;

    static Member testMember;

    @BeforeAll
    static void beforeAll() {
        testMember = Member.builder()
                .nickname("user1")
                .email("asd")
                .password("password").build();
    }

    @Test
    @DisplayName("회원 랭킹 조회 쿼리")
    void ranking() {
        testMember = memberRepository.save(testMember);

        for (int i = 0; i < 2; i++) {
            Member member = Member.builder().nickname("user" + i).email("asd")
                    .password("pass")
                    .build();
            memberRepository.save(member);
            Ranking ranking = Ranking.builder().member(member).build();
            ranking.saveWinScore();
            ranking.saveWinScore();
            rankingRepository.save(ranking);
        }

        for (int i = 0; i < 2; i++) {
            Member member = Member.builder().nickname("user" + i)
                    .email("asd")
                    .password("pass")
                    .build();
            memberRepository.save(member);
            rankingRepository.save(Ranking.builder().member(member).build());
        }
        Ranking ranking = Ranking.builder().member(testMember).build();
        ranking.saveWinScore();
        rankingRepository.save(ranking);

        long memberRanking = rankingRepository.findMemberRanking(testMember.getId());

        assertEquals(3, memberRanking);
    }
}