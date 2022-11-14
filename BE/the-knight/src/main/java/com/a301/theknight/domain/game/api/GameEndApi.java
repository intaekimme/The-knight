package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.end.response.GameEndResponse;
import com.a301.theknight.domain.game.service.GameExecuteEndService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameEndApi {

    private final SendMessageService messageService;
    private final GameExecuteEndService gameExecuteEndService;

    @MessageMapping(value = "/games/{gameId}/end")
    public void gameEnd(@DestinationVariable long gameId) {

        GameEndResponse gameEndResponse = gameExecuteEndService.gameEnd(gameId);

        messageService.sendData(gameId, "/end", gameEndResponse);

        messageService.proceedCall(gameId, 5000);
    }


}
