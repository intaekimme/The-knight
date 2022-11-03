package com.a301.theknight.domain.game.dto.prepare.request;

import lombok.Data;

@Data
public class GameOrderRequest {
    private int orderNumber;

    public boolean validate(int capacity) {
        return orderNumber >= 1 && orderNumber <= capacity;
    }
}
