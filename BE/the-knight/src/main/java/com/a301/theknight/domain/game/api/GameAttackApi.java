package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.attack.response.AttackResponse;
import com.a301.theknight.domain.game.dto.attack.response.AttackResponseDto;
import com.a301.theknight.domain.game.service.GameAttackService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class GameAttackApi {
    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;

    private final GameAttackService gameAttackService;

    @MessageMapping(value = "/games/{gameId}/attack")
    public void attack(@DestinationVariable long gameId, GameAttackRequest gameAttackRequest,
                       @LoginMemberId long memberId){
        AttackResponseDto response = gameAttackService.attack(gameId, memberId, gameAttackRequest);

        sendAttackResponse(gameId, response);
    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }

    public void sendAttackResponse(long gameId, AttackResponseDto attackResponseDto){
        if (attackResponseDto.getAllyResponse().getTeam().equals("A")) {
            template.convertAndSend(makeDestinationUri(gameId, "/a/attack"), AttackResponse.toResponse(attackResponseDto.getAllyResponse()));
            template.convertAndSend(makeDestinationUri(gameId, "/b/attack"), AttackResponse.toResponse(attackResponseDto.getOppResponse()));
        } else {
            template.convertAndSend(makeDestinationUri(gameId, "/b/attack"), AttackResponse.toResponse(attackResponseDto.getAllyResponse()));
            template.convertAndSend(makeDestinationUri(gameId, "/a/attack"), AttackResponse.toResponse(attackResponseDto.getOppResponse()));
        }
    }
}
