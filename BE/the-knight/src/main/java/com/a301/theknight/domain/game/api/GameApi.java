package com.a301.theknight.domain.game.api;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameApi {

    private final SimpMessagingTemplate template;

    @MessageMapping(value="/games/{gameId}/leader")
    public void leader(@DestinationVariable long gameId){
        StringBuilder sb = new StringBuilder();
        sb.append("/sub/games/").append(String.valueOf(gameId)).append("/leader");
        String destination = sb.toString();

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/prepare-time")
    public void prepareTime(@DestinationVariable long gameId){
        StringBuilder sb = new StringBuilder();
        sb.append("/sub/games/").append(String.valueOf(gameId)).append("/prepare-time");
        String destination = sb.toString();

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/attacker")
    public void attacker(@DestinationVariable long gameId){
        StringBuilder sb = new StringBuilder();
        sb.append("/sub/games/").append(String.valueOf(gameId)).append("/attacker");
        String destination = sb.toString();

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/pass")
    public void pass(@DestinationVariable long gameId){
        StringBuilder sb = new StringBuilder();
        sb.append("/sub/games/").append(String.valueOf(gameId)).append("/pass");
        String destination = sb.toString();

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/doubt")
    public void doubt(@DestinationVariable long gameId){
        StringBuilder sb = new StringBuilder();
        sb.append("/sub/games/").append(String.valueOf(gameId)).append("/doubt");
        String destination = sb.toString();

        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/turn")
    public void turn(@DestinationVariable long gameId){
        StringBuilder sb = new StringBuilder();
        sb.append("/sub/games/").append(String.valueOf(gameId)).append("/turn");
        String destination = sb.toString();

        template.convertAndSend(destination);
    }



}
