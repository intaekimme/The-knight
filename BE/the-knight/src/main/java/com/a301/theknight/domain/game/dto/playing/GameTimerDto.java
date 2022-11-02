package com.a301.theknight.domain.game.dto.playing;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameTimerDto {
    private int delay;
    private int second;
}
