package com.a301.theknight.domain.game.dto.playing.response;

import com.a301.theknight.domain.game.dto.playing.PlayerStateDto;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class GameMembersInfoDto {
    private int peopleNum;
    private Map<String, PlayerStateDto> teamA;
    private Map<String, PlayerStateDto> teamB;
}
