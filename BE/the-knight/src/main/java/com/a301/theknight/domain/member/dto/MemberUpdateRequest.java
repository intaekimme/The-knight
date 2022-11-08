package com.a301.theknight.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MemberUpdateRequest {
    @NotBlank
    @Size(min = 1, max = 45, message = "Nickname's length is not valid.")
    private String nickname;
    private String image;
}
