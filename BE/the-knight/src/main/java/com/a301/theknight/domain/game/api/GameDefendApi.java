package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponse;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponseDto;
import com.a301.theknight.domain.game.service.GameDefenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class GameDefendApi {
    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;
    private final GameDefenseService gameDefenseService;

    @MessageMapping(value = "/games/{gameId}/defense")
    public void defense(@DestinationVariable long gameId, GameDefenseRequest gameDefenseRequest,
                        @LoginMemberId long memberId){
        DefenseResponseDto response = gameDefenseService.defense(gameId, memberId, gameDefenseRequest);

        sendDefenseResponse(gameId, response);
    }
    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }
    public void sendDefenseResponse(long gameId, DefenseResponseDto defenseResponseDto){
        if (defenseResponseDto.getAllyResponse().getTeam().equals("A")) {
            template.convertAndSend(makeDestinationUri(gameId, "/a/attack"), DefenseResponse.toResponse(defenseResponseDto.getAllyResponse()));
            template.convertAndSend(makeDestinationUri(gameId, "/b/attack"), DefenseResponse.toResponse(defenseResponseDto.getOppResponse()));
        } else {
            template.convertAndSend(makeDestinationUri(gameId, "/b/attack"), DefenseResponse.toResponse(defenseResponseDto.getAllyResponse()));
            template.convertAndSend(makeDestinationUri(gameId, "/a/attack"), DefenseResponse.toResponse(defenseResponseDto.getOppResponse()));
        }
    }
}
