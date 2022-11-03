package com.a301.theknight.domain.game.dto.doubt.response;

import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoubtPlayerDto {
    private long id;
    private Boolean isDead;

    public static DoubtPlayerDto toDto(InGamePlayer suspect) {
        return DoubtPlayerDto.builder()
                .id(suspect.getMemberId())
                .isDead(suspect.isDead()).build();
    }
}
