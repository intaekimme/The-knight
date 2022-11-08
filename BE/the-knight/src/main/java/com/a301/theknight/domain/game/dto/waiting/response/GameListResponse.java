package com.a301.theknight.domain.game.dto.waiting.response;

import com.a301.theknight.domain.game.dto.waiting.GameListDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GameListResponse {
    private List<GameListDto> games;

    @Builder
    public GameListResponse(List<GameListDto> games){
        this.games = games;
    }
}
