package com.a301.theknight.domain.game.dto.playing;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PlayerStateDto {
    private long memberId;
    private String nickname;
    private int leftCount;
    private int rightCount;
    private List<String> weapons;
}
