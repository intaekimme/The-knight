package com.a301.theknight.domain.game.dto.defense.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefenseResponseDto {
    private DefenseTeamResponse allyResponse;
    private DefenseTeamResponse oppResponse;
}
