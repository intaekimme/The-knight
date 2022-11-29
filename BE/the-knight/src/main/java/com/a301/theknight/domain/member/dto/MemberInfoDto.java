package com.a301.theknight.domain.member.dto;

import com.a301.theknight.domain.player.entity.Player;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberInfoDto {
    private String nickname;
    private String image;

    public static MemberInfoDto toDto(Player player) {
        return MemberInfoDto.builder()
                .nickname(player.getMember().getNickname())
                .image(player.getMember().getImage()).build();
    }
}
