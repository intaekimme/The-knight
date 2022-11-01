package com.a301.theknight.domain.game.dto.playing.response;

import lombok.Data;

@Data
public class GameSelectResponse {
    private boolean selected;

    public GameSelectResponse(boolean selected) {
        this.selected = selected;
    }
}
