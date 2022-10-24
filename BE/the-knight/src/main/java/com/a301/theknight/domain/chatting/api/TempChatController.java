package com.a301.theknight.domain.chatting.api;

import com.a301.theknight.domain.chatting.entity.TempMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TempChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    private TempMessage receivePublicMessage(@Payload TempMessage message){
        System.out.println(message);
        return message;
    }

    @MessageMapping("/private-message")
    private TempMessage receivePrivateMessage(@Payload TempMessage message){
        System.out.println(message.getReceiver() + "/" + message);
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", message);
        System.out.println(message.getReceiver() + "/" + message);
        return message;
    }
}
