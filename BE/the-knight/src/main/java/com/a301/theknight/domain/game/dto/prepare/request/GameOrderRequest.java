package com.a301.theknight.domain.game.dto.prepare.request;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class GameOrderRequest {
    @Positive(message = "OrderNumber(${validatedValue}) must be a positive number.")
    private int orderNumber;

    public boolean validate(int capacity) {
        return orderNumber >= 1 && orderNumber <= capacity;
    }
}
