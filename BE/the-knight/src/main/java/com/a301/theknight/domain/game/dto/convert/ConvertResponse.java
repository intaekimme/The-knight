package com.a301.theknight.domain.game.dto.convert;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConvertResponse {
    private String postfix;
    private String gameStatus;
}
