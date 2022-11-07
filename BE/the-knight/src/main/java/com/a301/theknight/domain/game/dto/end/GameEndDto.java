package com.a301.theknight.domain.game.dto.end;

import com.a301.theknight.domain.game.dto.end.response.EndResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameEndDto {
    private EndResponse endResponseA;
    private EndResponse endResponseB;
}
