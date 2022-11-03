package com.a301.theknight.domain.game.dto.prepare.response;

import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GamePlayersInfoDto {
    private int maxUser;
    private List<PlayerDataDto> players;
}
