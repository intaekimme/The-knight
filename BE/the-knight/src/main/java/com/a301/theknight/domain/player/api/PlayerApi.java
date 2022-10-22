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

    private final SimpMessagingTemplate template;

    @MessageMapping(value="/games/{gameId}/entry")
    public void entry(@DestinationVariable long gameId){
        StringBuilder sb = new StringBuilder();
        sb.append("/sub/games/").append(String.valueOf(gameId)).append("/entry");
        String destination = sb.toString();

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/exit")
    public void exit(@DestinationVariable long gameId){
        StringBuilder sb = new StringBuilder();
        sb.append("/sub/games/").append(String.valueOf(gameId)).append("/exit");
        String destination = sb.toString();

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/team")
    public void team(@DestinationVariable long gameId, PlayerTeamDto playerTeamMessage){
        StringBuilder sb = new StringBuilder();
        sb.append("/sub/games/").append(String.valueOf(gameId)).append("/team");
        String destination = sb.toString();

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/ready")
    public void ready(@DestinationVariable long gameId, PlayerReadyDto playerReadyMessage){
        StringBuilder sb = new StringBuilder();
        sb.append("/sub/games/").append(String.valueOf(gameId)).append("/ready");
        String destination = sb.toString();

        template.convertAndSend(destination);
    }


}
