package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.end.GameEndDto;
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

        GameEndDto gameEndDto = gameExecuteEndService.gameEnd(gameId);

        messageService.sendData(gameId, "/a/end", gameEndDto.getEndResponseA());
        messageService.sendData(gameId, "/b/end", gameEndDto.getEndResponseB());

        //Proceed
        messageService.proceedCall(gameId, 5000);
    }


}
