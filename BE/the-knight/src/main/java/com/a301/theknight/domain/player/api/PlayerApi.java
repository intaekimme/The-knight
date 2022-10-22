package com.a301.theknight.domain.player.api;

import com.a301.theknight.domain.player.dto.PlayerReadyDto;
import com.a301.theknight.domain.player.dto.PlayerTeamDto;
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

    @MessageMapping(value="/games/{gameId}/entry")
    public void entry(@DestinationVariable long gameId){
        String destination = makeDestinationString(gameId, "/entry");

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/exit")
    public void exit(@DestinationVariable long gameId){
        String destination = makeDestinationString(gameId, "/exit");

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/team")
    public void team(@DestinationVariable long gameId, PlayerTeamDto playerTeamMessage){
        String destination = makeDestinationString(gameId, "/team");

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/ready")
    public void ready(@DestinationVariable long gameId, PlayerReadyDto playerReadyMessage){
        String destination = makeDestinationString(gameId, "/ready");

        template.convertAndSend(destination);
    }

    private static String makeDestinationString(long gameId, String postfix){
        StringBuilder sb = new StringBuilder();
        sb.append(SEND_PREFIX).append(gameId).append(postfix);
        return sb.toString();
    }

}
