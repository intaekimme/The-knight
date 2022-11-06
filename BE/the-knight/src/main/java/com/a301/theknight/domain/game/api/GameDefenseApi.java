package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponse;
import com.a301.theknight.domain.game.service.GameDefenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class GameDefenseApi {
    private static final String SEND_PREFIX = "/sub/games/";
    private static final String SERVER_PREFIX = "/pub/games/";
    private final SimpMessagingTemplate template;
    private final GameDefenseService gameDefenseService;

    @MessageMapping(value = "/games/{gameId}/defense")
    public void defense(@DestinationVariable long gameId, GameDefenseRequest gameDefenseRequest,
                        @LoginMemberId long memberId){
        gameDefenseService.defense(gameId, memberId, gameDefenseRequest);

        template.convertAndSend(makeConvertUri(gameId));
    }

    @MessageMapping(value = "/games/{gameId}//defense-info")
    public void defendInfo(@DestinationVariable long gameId) throws  InterruptedException {
        DefenseResponse response = gameDefenseService.getDefenseInfo(gameId);
        template.convertAndSend(makeDestinationUri(gameId, "/defense-info"), response);

        Thread.sleep(500);
        template.convertAndSend(makeDestinationUri(gameId, "/proceed"));
    }
    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }
    private String makeConvertUri(long gameId){ return SERVER_PREFIX + gameId + "/convert"; }
}
