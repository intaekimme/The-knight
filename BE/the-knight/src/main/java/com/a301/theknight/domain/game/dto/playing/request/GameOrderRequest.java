package com.a301.theknight.domain.game.dto.playing.request;

import lombok.Data;

@Data
public class GameOrderRequest {
    private int orderNumber;

    public boolean validate(long capacity) {
        return orderNumber >= 1 && orderNumber <= capacity;
    }
}
