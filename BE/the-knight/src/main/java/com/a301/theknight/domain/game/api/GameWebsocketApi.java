package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.GameModifyRequest;
import com.a301.theknight.domain.game.service.GameWebsocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebsocketApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;

    private final GameWebsocketService gameWebsocketService;

    @MessageMapping(value = "/games/{gameId}/modify")
    public void modify(@DestinationVariable long gameId,
                       @LoginMemberId long memberId,
                       GameModifyRequest gameModifyRequest){
        String destination = makeDestinationString(gameId, "/modify");
        gameWebsocketService.modify(gameId, memberId, gameModifyRequest);

        //TODO 수정 이후 어느 메시지를 담아 어느 브로커에 보낼지 생각하기
        template.convertAndSend(destination);
    }

    @MessageMapping(value = "/games/{gameId}/delete")
    public void delete(@DestinationVariable long gameId, @LoginMemberId long memberId){
        String destination = makeDestinationString(gameId, "/delete");
        gameWebsocketService.delete(gameId, memberId);

        //TODO 삭제 이후 어느 메시지를 담아 어느 브로커에 보낼지 생각하기
        template.convertAndSend(destination);
    }

    @MessageMapping(value="/games/{gameId}/leader")
    public void leader(@DestinationVariable long gameId, GameModifyRequest gameModifyRequest){
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
