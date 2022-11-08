package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.game.dto.execute.response.GameExecuteResponse;
import com.a301.theknight.domain.game.service.GameExecuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.Min;

@RequiredArgsConstructor
@Controller
public class GameExecuteApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private static final String SERVER_PREFIX = "/pub/games/";

    private final SimpMessagingTemplate template;
    private final GameExecuteService gameExecuteService;

    @MessageMapping(value = "/games/{gameId}/execute")
    public void executeTurn(@Min(1) @DestinationVariable long gameId) throws InterruptedException {
        GameExecuteResponse executeResponse = gameExecuteService.executeTurn(gameId);

        //DATA전송 -> 누가 누구를 뭘로 공격하는지
        template.convertAndSend(makeDestinationUri(gameId, "/execute"), executeResponse);

        //Proceed
        Thread.sleep(5000);
        template.convertAndSend(makeProceedUri(gameId));

        //convert 다시 실행
        Thread.sleep(5000);
        template.convertAndSend(makeConvertUri(gameId));
    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }

    private String makeConvertUri(long gameId){ return SERVER_PREFIX + gameId + "/convert"; }

    private String makeProceedUri(long gameId) {
        return SERVER_PREFIX + gameId + "/proceed";
    }
}