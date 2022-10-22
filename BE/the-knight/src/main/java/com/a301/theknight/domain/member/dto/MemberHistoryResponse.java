package com.a301.theknight.domain.member.dto;

import lombok.Data;

import java.util.List;

@Data
public class MemberHistoryResponse {
    private List<GameHistoryDto> games;
}
