package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.convert.PostfixDto;
import com.a301.theknight.domain.game.util.GameConvertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.Min;

@RequiredArgsConstructor
@Controller
public class GameConvertApi {

    private final SendMessageService sendMessageService;
    private final GameConvertUtil gameConvertUtil;

    @MessageMapping(value = "/games/{gameId}/convert-complete")
    public void convertComplete(@Min(1) @DestinationVariable long gameId) {
        PostfixDto postfixDto = gameConvertUtil.completeConvertPrepare(gameId);
        if (postfixDto != null) {
            sendMessageService.sendData(gameId, "/complete", postfixDto);
        }
    }
}
