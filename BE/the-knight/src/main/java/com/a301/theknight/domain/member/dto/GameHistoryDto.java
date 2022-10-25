package com.a301.theknight.domain.member.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GameHistoryDto {
    private long gameId;
    private String result;
    private int capacity;
    private int sword;
    private int twin;
    private int shield;
    private int hand;
    private List<MemberInfoDto> alliance;
    private List<MemberInfoDto> opposite;
}
