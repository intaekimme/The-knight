package com.a301.theknight.domain.game.dto.doubt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoubtPlayerIdDto {
    @Positive(message = "Id can only be positive.")
    private long memberId;
}
