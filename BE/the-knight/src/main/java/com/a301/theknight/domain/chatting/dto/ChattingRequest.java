package com.a301.theknight.domain.chatting.dto;

import com.a301.theknight.domain.chatting.entity.ChattingSet;
import com.a301.theknight.global.validation.ValidEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChattingRequest {
    @ValidEnum(enumClass = ChattingSet.class)
    private ChattingSet chattingSet;
    @NotNull
    @Size(min = 0, max=1000, message = "Valid content(${validatedValue}) length is between {min} and {max}.")
    private String content;
}
