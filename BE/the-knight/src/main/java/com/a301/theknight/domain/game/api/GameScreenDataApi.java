package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.factory.GameScreenDataServiceFactory;
import com.a301.theknight.domain.game.template.GameDataService;
import com.a301.theknight.domain.game.util.GameConvertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.Min;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameScreenDataApi {
    private final GameScreenDataServiceFactory dataTemplateFactory;
    private final GameConvertUtil gameConvertUtil;
    private final SendMessageService messageService;

    @MessageMapping(value = "/games/{gameId}/screen-data")
    public void makeAndSendScreenData(@Min(1) @DestinationVariable long gameId) {
        boolean isFullCount = gameConvertUtil.requestCounting(gameId);
        log.info("  [Request Count Flag = {}]", isFullCount);
        if (!isFullCount) {
            return;
        }

        GameDataService dataTemplate = dataTemplateFactory.getGameDataTemplate(gameId);
        if (dataTemplate == null) {
            gameConvertUtil.clearData(gameId);
            return;
        }
        dataTemplate.sendScreenData(gameId, messageService);
        dataTemplate.sendPlayersData(gameId, messageService);

        gameConvertUtil.initRequestQueue(gameId);
        messageService.proceedCall(gameId, 100);
    }

}
