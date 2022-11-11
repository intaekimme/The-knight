package com.a301.theknight.domain.game.dto.attacker.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttackerResponse {
    private long memberId;
    private boolean isOpposite;
}
