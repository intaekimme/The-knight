package com.a301.theknight.domain.game.dto.prepare.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GameStartRequest {
    //  TODO 안쓰는지 확인해서 안쓰면 지우기
    @NotBlank(message = "The game start string is required.")
    private String setGame;
}
