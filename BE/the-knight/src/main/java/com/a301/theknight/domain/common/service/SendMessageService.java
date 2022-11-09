package com.a301.theknight.domain.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SendMessageService {
    private static final String SEND_PREFIX = "/sub/games/";
    private static final String SERVER_PREFIX = "/pub/games/";
    private static final String CHAT_INFIX = "/chat-";
    private final SimpMessagingTemplate template;

    public void sendData(long gameId, String postfix, Object payload) {
        template.convertAndSend(makeDestinationUrl(gameId, postfix), payload);
    }

    public void sendChatData(long gameId, String chattingSet, Object payload) {
        template.convertAndSend(makeChatDestinationUrl(gameId, chattingSet), payload);
    }

    public void sendDataToServer(long gameId, String postfix) {
        template.convertAndSend(makeServerDestinationUrl(gameId, postfix), "");
    }

    public void convertCall(long gameId) {
        template.convertAndSend(makeConvertURL(gameId), "");
    }

    public void convertCall(long gameId, long delayMillis) {
        try {
            Thread.sleep(delayMillis);
            convertCall(gameId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void forceConvertCall(long gameId) {
        template.convertAndSend(makeForceConvertURL(gameId), "");
    }


    public void proceedCall(long gameId, long delayMillis) {
        try {
            Thread.sleep(delayMillis);

            template.convertAndSend(makeProceedURL(gameId), "");
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

    private String makeServerDestinationUrl(long gameId, String postfix) {
        return SERVER_PREFIX + gameId + postfix;
    }

    private String makeConvertURL(long gameId) {
        return SERVER_PREFIX + gameId + "/convert";
    }

    private String makeForceConvertURL(long gameId) {
        return SERVER_PREFIX + gameId + "/force-convert";
    }

    private String makeProceedURL(long gameId) {
        return SERVER_PREFIX + gameId + "/proceed";
    }
}
