package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.waiting.request.GameModifyRequest;
import com.a301.theknight.domain.game.dto.waiting.response.GameMembersInfoDto;
import com.a301.theknight.domain.game.dto.waiting.response.GameModifyResponse;
import com.a301.theknight.domain.game.service.GameWaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequiredArgsConstructor
public class GameWaitingApi {

    private final SendMessageService messageService;
    private final GameWaitingService gameWaitingService;

    @MessageMapping(value = "/games/{gameId}/modify")
    public void modify(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId,
                       @Valid GameModifyRequest gameModifyRequest) {
        GameModifyResponse gameModifyResponse = gameWaitingService.modify(gameId, memberId, gameModifyRequest);

        messageService.sendData(gameId,"/modify", gameModifyResponse);
    }

    @MessageMapping(value = "/games/{gameId}/members")
    public void getGameMemberData(@Min(1) @DestinationVariable long gameId) {
        GameMembersInfoDto membersInfo = gameWaitingService.getMembersInfo(gameId);

        messageService.sendData(gameId, "/members", membersInfo);
    }

}
