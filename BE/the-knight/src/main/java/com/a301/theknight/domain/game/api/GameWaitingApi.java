package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.waiting.request.GameModifyRequest;
import com.a301.theknight.domain.game.dto.waiting.response.GameMembersInfoDto;
import com.a301.theknight.domain.game.service.GameWaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWaitingApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;

    private final GameWaitingService gameWaitingService;

    @MessageMapping(value = "/games/{gameId}/modify")
    public void modify(@DestinationVariable long gameId,
                       @LoginMemberId long memberId,
                       GameModifyRequest gameModifyRequest){
        gameWaitingService.modify(gameId, memberId, gameModifyRequest);
        String destination = makeDestinationString(gameId, "/modify");

        template.convertAndSend(destination, "");
    }

    @MessageMapping(value = "/games/{gameId}/delete")
    public void delete(@DestinationVariable long gameId, @LoginMemberId long memberId){
        gameWaitingService.delete(gameId, memberId);
        String destination = makeDestinationString(gameId, "/delete");

        template.convertAndSend(destination, "");
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

    @MessageMapping(value="/games/{gameId}/turn")
    public void turn(@DestinationVariable long gameId){
        String destination = makeDestinationString(gameId, "/turn");

        template.convertAndSend(destination);
    }

    @MessageMapping(value = "/games/{gameId}/members")
    public void getGameMemberData(@DestinationVariable long gameId) {
        GameMembersInfoDto membersInfo = gameWaitingService.getMembersInfo(gameId);

        template.convertAndSend(makeDestinationString(gameId, "/members"), membersInfo);
    }


    private static String makeDestinationString(long gameId, String postfix){
        return SEND_PREFIX + gameId + postfix;
    }

}
