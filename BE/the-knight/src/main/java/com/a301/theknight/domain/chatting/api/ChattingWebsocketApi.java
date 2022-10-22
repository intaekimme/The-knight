package com.a301.theknight.domain.chatting.api;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChattingWebsocketApi {

    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/games/{gameId}/chat")
    public void message(@DestinationVariable long gameId){

    }
}
