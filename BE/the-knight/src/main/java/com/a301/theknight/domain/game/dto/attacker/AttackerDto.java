package com.a301.theknight.domain.game.dto.attacker;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttackerDto {
    private long memberId;
    private String team;
}
