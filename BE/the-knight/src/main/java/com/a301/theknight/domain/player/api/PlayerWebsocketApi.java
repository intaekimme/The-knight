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
        String destination = makeDestinationString(SEND_PREFIX, gameId, "/entry");
        template.convertAndSend(destination, playerEntryResponse);
    }

    @MessageMapping(value="/games/{gameId}/exit")
    public void exit(@DestinationVariable long gameId,
                     @LoginMemberId long memberId){
        long exitPlayerId = playerWebsocketService.exit(gameId, memberId);
        String destination = makeDestinationString(SEND_PREFIX, gameId, "/exit");
        template.convertAndSend(destination, exitPlayerId);
    }

    @MessageMapping(value="/games/{gameId}/team")
    public void team(@DestinationVariable long gameId,
                     @LoginMemberId long memberId,
                     PlayerTeamRequest playerTeamMessage){
        PlayerTeamResponse playerTeamResponse = playerWebsocketService.team(gameId, memberId, playerTeamMessage);
        String destination = makeDestinationString(SEND_PREFIX, gameId, "/team");
        template.convertAndSend(destination, playerTeamResponse);
    }

    @MessageMapping(value="/games/{gameId}/ready")
    public void ready(@DestinationVariable long gameId,
                      @LoginMemberId long memberId,
                      PlayerReadyRequest playerReadyMessage){
        ReadyResponseDto readyResponseDto = playerWebsocketService.ready(gameId, memberId, playerReadyMessage);

        String destination = makeDestinationString(SEND_PREFIX, gameId, "/ready");
        template.convertAndSend(destination, readyResponseDto.getPlayerReadyResponseList());
        if(readyResponseDto.isOwner()){
            destination = makeDestinationString("/pub/games/", gameId, "/start");
            template.convertAndSend(destination, readyResponseDto.getSetGame());
        }
    }

    private static String makeDestinationString(String prefix, long gameId, String postfix){
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(gameId).append(postfix);
        return sb.toString();
    }

}
