package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.game.dto.attacker.AttackerDto;
import com.a301.theknight.domain.game.service.GameAttackerService;
import com.a301.theknight.domain.common.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameAttackerApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SendMessageService messageService;
    private final GameAttackerService gameAttackerService;

    @MessageMapping(value = "/games/{gameId}/attacker")
    public void getAttacker(@DestinationVariable long gameId) {

        AttackerDto attackerDto = gameAttackerService.getAttacker(gameId);

        messageService.sendData(gameId, "/a/attacker", attackerDto.getAttackerResponseA());
        messageService.sendData(gameId, "/b/attacker", attackerDto.getAttackerResponseB());

        messageService.proceedCall(gameId, 5);
    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }
}
