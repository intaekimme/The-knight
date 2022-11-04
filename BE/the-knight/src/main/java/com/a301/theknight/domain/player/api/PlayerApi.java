package com.a301.theknight.domain.player.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.player.dto.*;
import com.a301.theknight.domain.player.dto.request.PlayerReadyRequest;
import com.a301.theknight.domain.player.dto.request.PlayerTeamRequest;
import com.a301.theknight.domain.player.dto.response.PlayerEntryResponse;
import com.a301.theknight.domain.player.dto.response.PlayerExitResponse;
import com.a301.theknight.domain.player.dto.response.PlayerTeamResponse;
import com.a301.theknight.domain.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PlayerApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;
    private final PlayerService playerService;

    @MessageMapping(value="/games/{gameId}/entry")
    public void entry(@DestinationVariable long gameId,
                      @LoginMemberId long memberId){
        PlayerEntryResponse playerEntryResponse = playerService.entry(gameId, memberId);
        String destination = makeDestinationString(SEND_PREFIX, gameId, "/entry");
        template.convertAndSend(destination, playerEntryResponse);
    }

    @MessageMapping(value="/games/{gameId}/exit")
    public void exit(@DestinationVariable long gameId,
                     @LoginMemberId long memberId){
        PlayerExitResponse exitPlayerId = playerService.exit(gameId, memberId);
        String destination = makeDestinationString(SEND_PREFIX, gameId, "/exit");
        template.convertAndSend(destination, exitPlayerId);
    }

    @MessageMapping(value="/games/{gameId}/team")
    public void team(@DestinationVariable long gameId,
                     PlayerTeamRequest playerTeamMessage,
                     @LoginMemberId long memberId){
        PlayerTeamResponse playerTeamResponse = playerService.team(gameId, memberId, playerTeamMessage);
        String destination = makeDestinationString(SEND_PREFIX, gameId, "/team");
        template.convertAndSend(destination, playerTeamResponse);
    }

    @MessageMapping(value="/games/{gameId}/ready")
    public void ready(@DestinationVariable long gameId,
                      @LoginMemberId long memberId,
                      PlayerReadyRequest playerReadyMessage){
        ReadyResponseDto readyResponseDto = playerService.ready(gameId, memberId, playerReadyMessage);

        String destination = makeDestinationString(SEND_PREFIX, gameId, "/ready");
        template.convertAndSend(destination, readyResponseDto);
        //TODO: 서버 -> 서버로 보내는 요청
    }

    private static String makeDestinationString(String prefix, long gameId, String postfix){
        return prefix + gameId + postfix;
    }

}
