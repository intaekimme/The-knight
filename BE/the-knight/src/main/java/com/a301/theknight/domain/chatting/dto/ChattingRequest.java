package com.a301.theknight.domain.chatting.dto;

import com.a301.theknight.domain.chatting.entity.ChattingSet;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChattingRequest {
    //  TODO Enum 타입 validation
    private ChattingSet chattingSet;
    @NotNull
    @Size(min = 0, max=1000, message = "Valid content(${validatedValue}) length is between {min} and {max}.")
    private String content;
}
