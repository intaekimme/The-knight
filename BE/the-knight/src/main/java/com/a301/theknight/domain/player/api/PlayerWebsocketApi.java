package com.a301.theknight.domain.player.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.player.dto.*;
import com.a301.theknight.domain.player.service.PlayerWebsocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PlayerWebsocketApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;
    private final PlayerWebsocketService playerWebsocketService;

    @MessageMapping(value="/games/{gameId}/entry")
    public void entry(@DestinationVariable long gameId,
                      @LoginMemberId long memberId){
        PlayerEntryResponse playerEntryResponse = playerWebsocketService.entry(gameId, memberId);
        String destination = makeDestinationString(gameId, "/entry");
        template.convertAndSend(destination, playerEntryResponse);
    }

    @MessageMapping(value="/games/{gameId}/exit")
    public void exit(@DestinationVariable long gameId,
                     @LoginMemberId long memberId){
        long exitPlayerId = playerWebsocketService.exit(gameId, memberId);
        String destination = makeDestinationString(gameId, "/exit");
        template.convertAndSend(destination, exitPlayerId);
    }

    @MessageMapping(value="/games/{gameId}/team")
    public void team(@DestinationVariable long gameId,
                     @LoginMemberId long memberId,
                     PlayerTeamRequest playerTeamMessage){
        PlayerTeamResponse playerTeamResponse = playerWebsocketService.team(gameId, memberId, playerTeamMessage);
        String destination = makeDestinationString(gameId, "/team");
        template.convertAndSend(destination, playerTeamResponse);
    }

    @MessageMapping(value="/games/{gameId}/ready")
    public void ready(@DestinationVariable long gameId,
                      @LoginMemberId long memberId,
                      PlayerReadyRequest playerReadyMessage){
        String destination = makeDestinationString(gameId, "/ready");
        ReadyResponseDto readyResponseDto = playerWebsocketService.ready(gameId, memberId, playerReadyMessage);
        template.convertAndSend(destination, readyResponseDto);
    }

    private static String makeDestinationString(long gameId, String postfix){
        StringBuilder sb = new StringBuilder();
        sb.append(SEND_PREFIX).append(gameId).append(postfix);
        return sb.toString();
    }

}
