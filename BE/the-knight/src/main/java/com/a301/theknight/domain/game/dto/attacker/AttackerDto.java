package com.a301.theknight.domain.game.dto.attacker;

import com.a301.theknight.domain.game.dto.attacker.response.AttackerResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttackerDto {
    private AttackerResponse attackerResponseA;
    private AttackerResponse attackerResponseB;
}
