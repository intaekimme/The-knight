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

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequiredArgsConstructor
public class GameWaitingApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;

    private final GameWaitingService gameWaitingService;

    @MessageMapping(value = "/games/{gameId}/modify")
    public void modify(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId,
                       @Valid GameModifyRequest gameModifyRequest) {
        gameWaitingService.modify(gameId, memberId, gameModifyRequest);

        template.convertAndSend(makeDestinationString(gameId, "/modify"), gameModifyRequest);
    }

    @MessageMapping(value = "/games/{gameId}/delete")
    public void delete(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId){
        gameWaitingService.delete(gameId, memberId);

        template.convertAndSend(makeDestinationString(gameId, "/delete"), "");
    }

    @MessageMapping(value="/games/{gameId}/turn")
    public void turn(@Min(1) @DestinationVariable long gameId){
        String destination = makeDestinationString(gameId, "/turn");

        template.convertAndSend(destination);
    }

    @MessageMapping(value = "/games/{gameId}/members")
    public void getGameMemberData(@Min(1) @DestinationVariable long gameId) {
        GameMembersInfoDto membersInfo = gameWaitingService.getMembersInfo(gameId);

        template.convertAndSend(makeDestinationString(gameId, "/members"), membersInfo);
    }


    private static String makeDestinationString(long gameId, String postfix){
        return SEND_PREFIX + gameId + postfix;
    }

}
