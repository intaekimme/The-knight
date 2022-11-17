package com.a301.theknight.domain.game.dto.doubt.response;

import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class DoubtPlayerDto {
    @Positive(message = "Id can only be positive.")
    private long memberId;
    @NotNull
    private String nickname;
    @NotNull(message = "Death information is required")
    private Boolean isDead;

    public static DoubtPlayerDto toDto(InGamePlayer suspect) {
        return DoubtPlayerDto.builder()
                .memberId(suspect.getMemberId())
                .nickname(suspect.getNickname())
                .isDead(suspect.isDead()).build();
    }
}
