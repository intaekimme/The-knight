package com.a301.theknight.domain.game.dto.doubt.response;

import com.a301.theknight.domain.game.entity.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DoubtResponseDto {
    DoubtResponse doubtResponse;
    GameStatus doubtStatus;
}
