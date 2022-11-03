package com.a301.theknight.domain.game.dto.doubt.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DoubtOpenDto {
    private long memberId;
    private String weapon;
    private String hand;
}