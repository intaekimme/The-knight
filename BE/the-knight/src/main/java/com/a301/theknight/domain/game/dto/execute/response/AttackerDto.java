package com.a301.theknight.domain.game.dto.execute.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AttackerDto {
    private long memberId;
    private String weapon;
    private String hand;
}
