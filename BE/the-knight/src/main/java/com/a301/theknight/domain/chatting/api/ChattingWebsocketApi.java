package com.a301.theknight.domain.chatting.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.chatting.dto.ChattingRequest;
import com.a301.theknight.domain.chatting.dto.ChattingResponse;
import com.a301.theknight.domain.chatting.entity.ChattingSet;
import com.a301.theknight.domain.chatting.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChattingWebsocketApi {

    private final SimpMessagingTemplate template;
    private final ChattingService chattingService;

    @MessageMapping(value = "/games/{gameId}/chat")
    public void sendMessage(ChattingRequest chattingRequest,
                            @DestinationVariable long gameId, @LoginMemberId long memberId) {
        ChattingResponse chattingResponse = chattingService.makeResponse(memberId, gameId, chattingRequest);

        String destinationUri = makeDestinationUri("/sub/games/", gameId, chattingResponse.getChattingSet());
        template.convertAndSend(destinationUri, chattingResponse);
    }

    private String makeDestinationUri(String prefix, long gameId, String chattingSet) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(gameId).append("/").append("chat-").append(chattingSet.toLowerCase());

        return sb.toString();
    }

}
