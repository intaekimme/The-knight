package com.a301.theknight.domain.game.api;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;

    @MessageMapping(value="/games/{gameId}/leader")
    public void leader(@DestinationVariable long gameId){
        String destination = makeDestinationString(gameId, "/leader");

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/prepare-time")
    public void prepareTime(@DestinationVariable long gameId){
        String destination = makeDestinationString(gameId, "/prepare-time");

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/attacker")
    public void attacker(@DestinationVariable long gameId){
        String destination = makeDestinationString(gameId, "/attacker");

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/pass")
    public void pass(@DestinationVariable long gameId){
        String destination = makeDestinationString(gameId, "/pass");

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/doubt")
    public void doubt(@DestinationVariable long gameId){
        String destination = makeDestinationString(gameId, "/doubt");

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/turn")
    public void turn(@DestinationVariable long gameId){
        String destination = makeDestinationString(gameId, "/turn");

        template.convertAndSend(destination);
    }

    private static String makeDestinationString(long gameId, String postfix){
        StringBuilder sb = new StringBuilder();
        sb.append(SEND_PREFIX).append(gameId).append(postfix);
        return sb.toString();
    }

}
