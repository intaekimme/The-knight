package com.a301.theknight.domain.ranking.dto;

import lombok.Data;

@Data
public class MemberRankingDto {
    private String nickname;
    private String image;
    public int ranking;
    public int score;
}
