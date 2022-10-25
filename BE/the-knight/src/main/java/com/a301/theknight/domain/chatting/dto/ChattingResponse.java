package com.a301.theknight.domain.chatting.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChattingResponse {
    private long memberId;
    private String nickname;
    private String chattingSet;
    private String content;
}
