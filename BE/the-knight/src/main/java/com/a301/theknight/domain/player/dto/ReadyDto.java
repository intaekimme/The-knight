package com.a301.theknight.domain.player.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReadyDto {
    private long memberId;
    private boolean readyStatus;
    private boolean canStart;
}
