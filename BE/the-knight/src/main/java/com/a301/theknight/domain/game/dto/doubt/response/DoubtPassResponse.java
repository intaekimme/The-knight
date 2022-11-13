package com.a301.theknight.domain.game.dto.doubt.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoubtPassResponse {
    private long memberId;
    private String nickname;
}
