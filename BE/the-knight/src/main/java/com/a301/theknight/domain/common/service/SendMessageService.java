package com.a301.theknight.domain.common.service;

import com.a301.theknight.domain.game.dto.convert.ConvertResponse;
import com.a301.theknight.domain.game.dto.convert.ProceedResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.util.GameConvertUtil;
import com.a301.theknight.domain.limit.factory.TimeLimitServiceFactory;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendMessageService {
    private static final String SEND_PREFIX = "/sub/games/";
    private static final String CHAT_INFIX = "/chat-";

    private final SimpMessagingTemplate template;
    private final GameConvertUtil gameConvertUtil;
    private final TimeLimitServiceFactory timeLimitServiceFactory;

    public void sendData(long gameId, String postfix, Object payload) {
        log.info(" <<-- [Send Data] /sub/games/{}{}, value = {}", gameId, postfix, payload.toString());
        template.convertAndSend(makeDestinationUrl(gameId, postfix), payload);
    }

    public void sendChatData(long gameId, String chattingSet, Object payload) {
        log.info(" <<-- [Send Data] /sub/games/{}/chat-{}, value = {}", gameId, chattingSet, payload.toString());
        template.convertAndSend(makeChatDestinationUrl(gameId, chattingSet), payload);
    }

    public void convertCall(long gameId) {
        ConvertResponse response = gameConvertUtil.convertScreen(gameId);
        if (response != null) {
            log.info(" <<-- [Convert] Pre = {}, Next = {}", response.getPreStatus(), response.getGameStatus());
            sendData(gameId, "/convert", response);
        }
    }

    public void convertCall(long gameId, long delayMillis) {
        try {
            Thread.sleep(delayMillis);
            convertCall(gameId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void proceedCall(long gameId, long delayMillis) {
        try {
            Thread.sleep(delayMillis);
            GameStatus gameStatus = gameConvertUtil.getGameStatus(gameId);

            sendData(gameId, "/proceed", ProceedResponse.toDto(gameStatus));
            TimeLimitServiceTemplate timeLimitService = timeLimitServiceFactory.getTimeLimitService(gameStatus);
            timeLimitService.executeTimeLimit(gameId, this, gameStatus);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String makeDestinationUrl(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }

    private String makeChatDestinationUrl(long gameId, String chattingSet) {
        return SEND_PREFIX + gameId + CHAT_INFIX + chattingSet.toLowerCase();
    }

}
