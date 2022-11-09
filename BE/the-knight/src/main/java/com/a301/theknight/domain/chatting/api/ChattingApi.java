package com.a301.theknight.domain.chatting.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.chatting.dto.ChattingRequest;
import com.a301.theknight.domain.chatting.dto.ChattingResponse;
import com.a301.theknight.domain.chatting.service.ChattingService;
import com.a301.theknight.domain.common.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequiredArgsConstructor
public class ChattingApi {

    private final SendMessageService messageService;
    private final ChattingService chattingService;

    @MessageMapping(value = "/games/{gameId}/chat")
    public void sendMessage(@Valid ChattingRequest chattingRequest,
                            @Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId) {
        ChattingResponse chattingResponse = chattingService.makeResponse(memberId, gameId, chattingRequest);

        messageService.sendChatData(gameId, chattingResponse.getChattingSet(), chattingResponse);
    }

}
