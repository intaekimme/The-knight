package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.convert.GameStatusResponse;
import com.a301.theknight.domain.game.service.GameConvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.Min;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class GameConvertApi {

    private final SendMessageService sendMessageService;
    private final GameConvertService gameConvertService;

    @MessageMapping(value = "/games/{gameId}/convert")
    public void publishConvert(@Min(1) @DestinationVariable long gameId) {
        GameStatusResponse gameStatusResponse = gameConvertService.getGameStatus(gameId);
        sendMessageService.sendData(gameId,"/convert",gameStatusResponse);
    }

    @MessageMapping(value = "/games/{gameId}/force-convert")
    public void forceConvert(@DestinationVariable long gameId) {
        GameStatusResponse gameStatusResponse = gameConvertService.getNextGameStatus(gameId);
        sendMessageService.sendData(gameId,"/convert",gameStatusResponse);
    }

    @MessageMapping(value = "/games/{gameId}/convert-complete")
    public void convertComplete(@Min(1) @DestinationVariable long gameId) {
        List<String> postfixList = gameConvertService.convertComplete(gameId);
        if (postfixList != null) {
            postfixList.forEach(postfix -> sendMessageService.sendDataToServer(gameId, postfix));
        }
    }

    @MessageMapping(value = "/games/{gameId}/proceed")
    public void proceedGame(@Min(1) @DestinationVariable long gameId) {
        GameStatusResponse gameStatus = gameConvertService.getGameStatus(gameId);
        sendMessageService.sendData(gameId, "/proceed", gameStatus);
    }

}
