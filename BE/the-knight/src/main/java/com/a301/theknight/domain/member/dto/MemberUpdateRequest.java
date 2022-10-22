package com.a301.theknight.domain.member.dto;

import lombok.Data;

@Data
public class MemberUpdateRequest {
    private String nickname;
    private String image;
}
