package com.a301.theknight.domain.game.util;

import com.a301.theknight.domain.game.dto.prepare.response.GameCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.TimerTask;

@RequiredArgsConstructor
public class SendTimeTask extends TimerTask {

    private final SimpMessagingTemplate template;
    private final int maxSeconds;
    private final String destination;
    private int count = 0;

    @Override
    public void run() {
        if (count <= maxSeconds) {
            template.convertAndSend(destination, new GameCountDto(maxSeconds - count));
            count++;
        } else {
            this.cancel();
        }
    }
}
