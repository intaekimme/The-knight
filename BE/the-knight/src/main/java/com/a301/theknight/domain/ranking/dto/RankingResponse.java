package com.a301.theknight.domain.ranking.dto;

import lombok.Data;

import java.util.List;

@Data
public class RankingResponse {
    private List<RankingDto> rankings;
}
