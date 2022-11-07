package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.doubt.request.GameDoubtPassRequest;
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
    private static final String SERVER_PREFIX = "/pub/games/";

    private final SimpMessagingTemplate template;
    private final GameDoubtService gameDoubtService;

    @MessageMapping(value = "/games/{gameId}/doubt")
    public void doubt(@DestinationVariable long gameId, GameDoubtRequest doubtRequest,
                      @LoginMemberId long memberId) {
        gameDoubtService.doubt(gameId, memberId, doubtRequest.getSuspected().getId(), doubtRequest.getDoubtStatus());

        template.convertAndSend(makeConvertUri(gameId));
    }

    @MessageMapping(value = "/games/{gameId}/doubt-info")
    public void doubtInfo(@DestinationVariable long gameId) throws InterruptedException {
        DoubtResponse doubtResponse = gameDoubtService.getDoubtInfo(gameId);
        template.convertAndSend(makeDestinationUri(gameId, "/doubt-info"), doubtResponse);

        Thread.sleep(5000);
        template.convertAndSend(makeProceedUri(gameId));

        Thread.sleep(10000);
        template.convertAndSend(makeConvertUri(gameId));
    }

    @MessageMapping(value="/games/{gameId}/doubt-pass")
    public void doubtPass(@DestinationVariable long gameId, GameDoubtPassRequest gameDoubtPassRequest,
                          @LoginMemberId long memberId){
        gameDoubtService.doubtPass(gameId, memberId,  gameDoubtPassRequest.getDoubtStatus());
        template.convertAndSend(makeConvertUri(gameId));
    }

    private String makeProceedUri(long gameId) {
        return SERVER_PREFIX + gameId + "/proceed";
    }

    private String makeConvertUri(long gameId){ return SERVER_PREFIX + gameId + "/convert"; }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }

}
