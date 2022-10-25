package com.a301.theknight.domain.chatting.dto;

import com.a301.theknight.domain.chatting.entity.ChattingSet;
import lombok.Data;

@Data
public class ChattingRequest {
    private ChattingSet chattingSet;
    private String content;
}
