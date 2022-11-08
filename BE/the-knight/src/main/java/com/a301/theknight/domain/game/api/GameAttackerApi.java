package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.game.dto.attacker.AttackerDto;
import com.a301.theknight.domain.game.service.GameAttackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameAttackerApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private static final String SERVER_PREFIX = "/pub/games/";
    private final SimpMessagingTemplate template;
    private final GameAttackerService gameAttackerService;

    @MessageMapping(value = "/games/{gameId}/attacker")
    public void getAttacker(@DestinationVariable long gameId) throws InterruptedException {

        AttackerDto attackerDto = gameAttackerService.getAttacker(gameId);

        template.convertAndSend(makeDestinationUri(gameId, "/a/attacker"), attackerDto.getAttackerResponseA());
        template.convertAndSend(makeDestinationUri(gameId, "/b/attacker"), attackerDto.getAttackerResponseB());

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
