package com.a301.theknight.domain.player.dto.request;

import com.a301.theknight.domain.player.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerTeamRequest {
    //  TODO Enum 타입 valid 처리
    private Team team;
}
