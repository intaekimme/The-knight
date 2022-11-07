package com.a301.theknight.domain.player.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PlayerReadyRequest {
    @NotNull(message = "ReadyStatus must have a value.")
    private boolean readyStatus;
}
