package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.doubt.request.GameDoubtRequest;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtResponse;
import com.a301.theknight.domain.game.service.GameDoubtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class GameDoubtApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;

    private final GameDoubtService gameDoubtService;

    @MessageMapping(value = "/games/{gameId}/doubt")
    public void doubt(@DestinationVariable long gameId, GameDoubtRequest doubtRequest,
                      @LoginMemberId long memberId) {
        DoubtResponse response = gameDoubtService.doubt(gameId, memberId, doubtRequest.getSuspected().getId());

        template.convertAndSend(makeDestinationUri(gameId, "/doubt"), response);
    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }

}
