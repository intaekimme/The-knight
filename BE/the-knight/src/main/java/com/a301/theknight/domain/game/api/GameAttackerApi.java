package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.game.dto.attacker.AttackerDto;
import com.a301.theknight.domain.game.service.GameAttackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.Min;

@Controller
@RequiredArgsConstructor
public class GameAttackerApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;
    private final GameAttackerService gameAttackerService;

    @MessageMapping(value = "/games/{gameId}/attacker")
    public void getAttacker(@Min(1) @DestinationVariable long gameId) {

        AttackerDto attackerDto = gameAttackerService.getAttacker(gameId);

        template.convertAndSend(makeDestinationUri(gameId, "/a/attacker"), attackerDto.getAttackerResponseA());
        template.convertAndSend(makeDestinationUri(gameId, "/b/attacker"), attackerDto.getAttackerResponseB());
    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }
}
