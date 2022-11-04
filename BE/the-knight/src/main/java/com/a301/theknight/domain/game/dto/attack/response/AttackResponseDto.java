package com.a301.theknight.domain.game.dto.attack.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttackResponseDto {
    private AttackTeamResponse allyResponse;
    private AttackTeamResponse oppResponse;
}
