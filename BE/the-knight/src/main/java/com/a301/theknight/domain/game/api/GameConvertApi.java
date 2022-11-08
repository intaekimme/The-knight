package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.convert.GameStatusResponse;
import com.a301.theknight.domain.game.service.GameConvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.Min;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class GameConvertApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private static final String SERVER_PREFIX = "/pub/games/";
    private final SimpMessagingTemplate template;

    private final GameConvertService gameConvertService;

    @MessageMapping(value = "/games/{gameId}/convert")
    public void publishConvert(@Min(1) @DestinationVariable long gameId) {
        GameStatusResponse gameStatusResponse = gameConvertService.getGameStatus(gameId);

        template.convertAndSend(makeDestinationUri(gameId, "/convert"), gameStatusResponse);
    }

    @MessageMapping(value = "/games/{gameId}/convert-complete")
    public void convertComplete(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId) {
        List<String> postfixList = gameConvertService.convertComplete(gameId, memberId);
        if (postfixList != null) {
            postfixList.forEach(postfix -> template
                    .convertAndSend(makeServerDestinationUri(gameId, postfix)));
        }
    }

    @MessageMapping(value = "/games/{gameId}/proceed")
    public void proceedGame(@Min(1) @DestinationVariable long gameId) {
        GameStatusResponse gameStatus = gameConvertService.getGameStatus(gameId);
        template.convertAndSend(makeDestinationUri(gameId, "/proceed"), gameStatus);
    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }

    private String makeServerDestinationUri(long gameId, String postfix) {
        return SERVER_PREFIX + gameId + postfix;
    }
}
