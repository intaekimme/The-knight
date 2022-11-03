package com.a301.theknight.domain.game.dto.playing.response;

import com.a301.theknight.domain.game.dto.playing.PlayerDataDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class GameMembersInfoDto {
    private int maxUser;
    private List<PlayerDataDto> players;
}
