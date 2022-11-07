package com.a301.theknight.domain.player.dto;

import com.a301.theknight.domain.player.dto.response.PlayerReadyResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ReadyResponseDto {
    private long memberId;
    private boolean readyStatus;
}
