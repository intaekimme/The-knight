package com.a301.theknight.domain.game.dto.end.response;

import com.a301.theknight.domain.game.dto.end.PlayerWeaponDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EndResponse {
    private boolean isWin;
    private String losingTeam;
    private long losingLeaderId;
    private long winningLeaderId;
    private List<PlayerWeaponDto> players;
}
