package com.a301.theknight.domain.game.dto.convert;

import com.a301.theknight.domain.game.entity.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LimitTimeDto {
    private long limitTime;

    public static LimitTimeDto toDto(GameStatus gameStatus) {
        return new LimitTimeDto(gameStatus.getLimitSeconds());
    }
}
