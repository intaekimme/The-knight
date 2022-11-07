package com.a301.theknight.domain.game.dto.defense.request;

import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameDefenseRequest {
    @NotNull(message = "Defender information is required.")
    private DefendPlayerDto defender;

    //  TODO Enum 타입으로 바꾸고 valid 체크하기
    @NotNull
    private String weapon;
    @NotNull
    private String hand;
}
