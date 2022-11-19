package com.a301.theknight.domain.game.dto.convert;

import com.a301.theknight.domain.game.entity.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConvertResponse {
    private GameStatus preStatus;
    private GameStatus gameStatus;
}
