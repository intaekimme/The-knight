package com.a301.theknight.domain.game.dto.prepare.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class GameOrderDto implements Serializable {
    private long memberId;
    private String nickname;
    private String image;
}
