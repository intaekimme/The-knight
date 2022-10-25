package com.a301.theknight.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberInfoDto {
    private String nickname;
    private String image;
}
