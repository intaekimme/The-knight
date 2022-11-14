package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.factory.GameDataTemplateFactory;
import com.a301.theknight.domain.game.template.GameDataService;
import com.a301.theknight.domain.game.util.GameConvertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.Min;

//여기 서비스 로직에 템플릿 메소드 패턴을 넣어서
//요청 카운팅하기
//카운팅 완료 시 run()을 수행
//해당 응답을 각각의 큐에 맞게 전달
@Controller
@RequiredArgsConstructor
public class GameDataApi {
    private final GameDataTemplateFactory dataTemplateFactory;
    private final GameConvertUtil gameConvertUtil;
    private final SendMessageService messageService;

    @MessageMapping(value = {
            "/games/{gameId}/prepare", "/games/{gameId}/pre-attack",
            "/games/{gameId}/attacker", "/games/{gameId}/attack-info",
            "/games/{gameId}/defense-info", "/games/{gameId}/doubt-info",
            "/games/{gameId}/execute", "/games/{gameId}/end",
    })
    public void prepareGameStart(@Min(1) @DestinationVariable long gameId) {
        boolean isFullCount = gameConvertUtil.requestCounting(gameId);
        if (!isFullCount) {
            return;
        }

        GameDataService dataTemplate = dataTemplateFactory.getGameDataTemplate(gameId);

        dataTemplate.makeData(gameId);
        dataTemplate.sendScreenData(gameId, messageService);

        messageService.proceedCall(gameId, 500);
    }

}
