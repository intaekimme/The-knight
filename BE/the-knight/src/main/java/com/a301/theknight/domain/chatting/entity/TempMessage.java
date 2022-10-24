package com.a301.theknight.domain.chatting.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TempMessage {
    private String sender;
    private String receiver;
    private String message;
    private String date;
    private TempStatus status;
}
