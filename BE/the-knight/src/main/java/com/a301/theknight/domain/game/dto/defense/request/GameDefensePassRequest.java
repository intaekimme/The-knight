package com.a301.theknight.domain.game.dto.defense.request;

import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameDefensePassRequest {
    @NotNull(message = "Defender information is required.")
    private DefendPlayerDto defender;
}
