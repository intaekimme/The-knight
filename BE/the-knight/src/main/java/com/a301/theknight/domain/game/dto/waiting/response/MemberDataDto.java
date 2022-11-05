package com.a301.theknight.domain.game.dto.waiting.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberDataDto {
    private long id;
    private String nickname;
    private String image;
    private String team;
    private boolean readyStatus;
}
