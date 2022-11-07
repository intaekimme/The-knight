package com.a301.theknight.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberInfoResponse {
    private String nickname;
    private String image;
    private long ranking;
    private int score;
    private int win;
    private int lose;
}
