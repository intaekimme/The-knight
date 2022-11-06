package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.pass.response.PassResponse;
import com.a301.theknight.domain.game.service.GamePassService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class GamePassApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private static final String SERVER_PREFIX = "/pub/games/";

    private final SimpMessagingTemplate template;
    private final GamePassService gamePassService;

    @MessageMapping(value = "/games/{gameId}/pass")
    public void pass(@DestinationVariable long gameId, @LoginMemberId long memberId) {
        PassResponse response = gamePassService.pass(gameId, memberId);

        template.convertAndSend(makeDestinationUri(gameId, "/doubt"), response);
    }

    @MessageMapping(value = "/games/{gameId}/execute")
    public void executeTurn(@DestinationVariable long gameId) {
        gamePassService.executeTurn(gameId);

        template.convertAndSend(makeConvertUri(gameId));
    }

    @MessageMapping(value = "/games/{gameId}/turn-info")
    public void turnInfo(@DestinationVariable long gameId) {

    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }

    private String makeConvertUri(long gameId){ return SERVER_PREFIX + gameId + "/convert"; }

    private String makeProceedUri(long gameId) {
        return SERVER_PREFIX + gameId + "/proceed";
    }
}