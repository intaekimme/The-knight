package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.game.dto.end.GameEndDto;
import com.a301.theknight.domain.game.service.GameExecuteEndService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameEndApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private static final String SERVER_PREFIX = "/pub/games/";
    private final SimpMessagingTemplate template;
    private final GameExecuteEndService gameExecuteEndService;

    @MessageMapping(value = "/games/{gameId}/end")
    public void gameEnd(@DestinationVariable long gameId) throws InterruptedException {

        GameEndDto gameEndDto = gameExecuteEndService.gameEnd(gameId);

        template.convertAndSend(makeDestinationUri(gameId, "/a/end"), gameEndDto.getEndResponseA());
        template.convertAndSend(makeDestinationUri(gameId, "/b/end"), gameEndDto.getEndResponseB());

        //Proceed
        Thread.sleep(5000);
        template.convertAndSend(makeProceedUri(gameId));
    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }

    private String makeProceedUri(long gameId) {
        return SERVER_PREFIX + gameId + "/proceed";
    }
}
