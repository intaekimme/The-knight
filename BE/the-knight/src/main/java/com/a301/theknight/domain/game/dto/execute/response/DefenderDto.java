package com.a301.theknight.domain.game.dto.execute.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DefenderDto {
    private long id;
    private String hand;
    private Boolean isDead;
    private int restCount;
}
