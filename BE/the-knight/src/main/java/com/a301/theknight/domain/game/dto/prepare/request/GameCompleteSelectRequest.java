package com.a301.theknight.domain.game.dto.prepare.request;

import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.validation.ValidEnum;
import lombok.Data;

@Data
public class GameCompleteSelectRequest {
    //  TODO 안쓰면 삭제
    @ValidEnum(enumClass = Team.class)
    private Team team;
}
