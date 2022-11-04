package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.convert.GameStatusResponse;
import com.a301.theknight.domain.game.service.GameConvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class GameConvertApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;

    private final GameConvertService gameConvertService;

    @MessageMapping(value = "/games/{gameId}/convert")
    public void publishConvert(@DestinationVariable long gameId) {
        GameStatusResponse gameStatusResponse = gameConvertService.getGameStatus(gameId);

        template.convertAndSend(makeDestinationUri(gameId, "/convert"), gameStatusResponse);
    }

    @MessageMapping(value = "/games/{gameId}/convert-complete")
    public void ConvertComplete(@DestinationVariable long gameId, @LoginMemberId long memberId) {
        List<String> postfixList = gameConvertService.switchCount(gameId, memberId);
        if (postfixList == null) {
            return;
        }
        //TODO: State에 따른 다른 데이터들을 보내줘야 함.
        // -> Postfix List를 Service에서 응답 받아서 그 메시지들을 발행?
        template.convertAndSend(makeDestinationUri(gameId, "/"));
    }

    @MessageMapping(value = "/games/{gameId}/proceed")
    public void proceedGame(@DestinationVariable long gameId) {
        GameStatusResponse gameStatus = gameConvertService.getGameStatus(gameId);
        template.convertAndSend(makeDestinationUri(gameId, "/proceed"), gameStatus);
    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }
}
