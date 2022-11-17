package com.a301.theknight.domain.ranking.entity;

import com.a301.theknight.domain.common.entity.BaseTimeEntity;
import com.a301.theknight.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Ranking extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    private int score;

    private int win;

    private int lose;

    @Builder
    public Ranking(Member member) {
        this.member = member;
    }

    public void saveWinScore() {
        //TODO : 승점 기준 정하기
        score += 10;
        win++;
    }

    public void saveLoseScore() {
        //TODO : 점수 기준 정하기
        score -= 5;
        if (score < 0)
            score = 0;
        lose++;
    }
}
