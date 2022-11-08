package com.a301.theknight.domain.game.dto.doubt.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Data
public class DoubtPlayerIdDto {
    @Positive(message = "Id can only be positive.")
    private long id;
}
