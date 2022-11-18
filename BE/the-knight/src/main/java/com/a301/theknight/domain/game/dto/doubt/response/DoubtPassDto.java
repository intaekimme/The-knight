package com.a301.theknight.domain.game.dto.doubt.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DoubtPassDto {
    private boolean fullCount;
    private DoubtPassResponse doubtPassResponse;
}
