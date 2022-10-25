package com.a301.theknight.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class MemberHistoryResponse {
    private List<GameHistoryDto> games;
}
