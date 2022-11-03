package com.a301.theknight.domain.game.dto.playing;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PlayerDataDto {
    private long memberId;
    private String nickname;
    private int leftCount;
    private int rightCount;
    private int order;
    private List<String> weapons;
}
