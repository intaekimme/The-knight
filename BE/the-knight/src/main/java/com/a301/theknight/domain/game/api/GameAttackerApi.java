package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.game.dto.attacker.AttackerDto;
import com.a301.theknight.domain.game.dto.attacker.response.AttackerResponse;
import com.a301.theknight.domain.game.service.GameAttackerService;
import com.a301.theknight.domain.player.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameAttackerApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;
    private final GameAttackerService gameAttackerService;

    @MessageMapping(value = "/games/{gameId}/attacker")
    public void getAttacker(@DestinationVariable long gameId) {

        AttackerDto attackerDto = gameAttackerService.getAttacker(gameId);
        AttackerResponse responseA = AttackerResponse.builder().memberId(attackerDto.getMemberId()).isOpposite(attackerDto.getTeam().equals("A")).build();
        AttackerResponse responseB = AttackerResponse.builder().memberId(attackerDto.getMemberId()).isOpposite(attackerDto.getTeam().equals("B")).build();

        template.convertAndSend(makeDestinationUri(gameId, "/a/attacker"), responseA);
        template.convertAndSend(makeDestinationUri(gameId, "/b/attacker"), responseB);
    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }
}
