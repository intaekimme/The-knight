package com.a301.theknight.domain.game.dto.waiting.response;

import com.a301.theknight.domain.game.dto.waiting.GameListDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GameListResponse {
    private int maxPageNum;
    private List<GameListDto> games;

    @Builder
    public GameListResponse(int maxPageNum, List<GameListDto> games){
        this.maxPageNum = maxPageNum;
        this.games = games;
    }
}
