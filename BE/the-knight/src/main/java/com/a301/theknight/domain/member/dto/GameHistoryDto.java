package com.a301.theknight.domain.member.dto;

import java.util.List;

public class GameHistoryDto {
    private long gameId;
    private String result;
    private int capacity;
    private int sword;
    private int twin;
    private int shield;
    private int hand;
    private List<MemberInfoResponse> alliance;
    private List<MemberInfoResponse> opposite;
}
