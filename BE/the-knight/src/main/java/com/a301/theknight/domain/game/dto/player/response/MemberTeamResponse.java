package com.a301.theknight.domain.game.dto.player.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberTeamResponse {
    private long memberId;
    private String nickname;
    private String team;
}
