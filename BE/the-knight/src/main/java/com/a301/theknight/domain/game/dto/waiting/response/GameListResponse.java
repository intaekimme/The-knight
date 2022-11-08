package com.a301.theknight.domain.game.dto.waiting.response;

import com.a301.theknight.domain.game.dto.waiting.GameListDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameListResponse {
    private List<GameListDto> games;
}
