package com.a301.theknight.domain.player.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReadyResponseDto {
    private PlayerReadyResponseList playerReadyResponseList;
    private SetGame setGame;
    private boolean isOwner = false;
}
