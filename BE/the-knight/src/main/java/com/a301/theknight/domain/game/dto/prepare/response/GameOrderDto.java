package com.a301.theknight.domain.game.dto.prepare.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameOrderDto {
    private long memberId;
    private String nickname;
    private String image;
}
