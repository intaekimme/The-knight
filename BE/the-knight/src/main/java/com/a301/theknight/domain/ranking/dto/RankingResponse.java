package com.a301.theknight.domain.ranking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class RankingResponse {
    private List<RankingDto> rankings;
}
