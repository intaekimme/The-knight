package com.a301.theknight.domain.game.dto.playing.response;

import com.a301.theknight.domain.player.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class GameOrderResponse {
    private Team team;
    private GameOrderDto[] orderList;
}
