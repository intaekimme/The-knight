package com.a301.theknight.domain.game.dto.prepare.request;

import com.a301.theknight.domain.player.entity.Team;
import lombok.Data;

@Data
public class GameCompleteSelectRequest {
    //  TODO Enum 타입 예외 처리
    private Team team;
}
