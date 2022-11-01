package com.a301.theknight.domain.game.dto.waiting.response;

import com.a301.theknight.domain.game.dto.waiting.GameListDto;
import lombok.Data;

import java.util.List;

@Data
public class GameListResponse {
    private List<GameListDto> games;
}
