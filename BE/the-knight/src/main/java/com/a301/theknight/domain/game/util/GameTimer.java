package com.a301.theknight.domain.game.util;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Timer;

public class GameTimer {

    private final static int MILLI = 1000;

    private Timer timer;
    private SendTimeTask timerTask;

    public void sendSeconds(int delay, int maxSeconds, String destination, SimpMessagingTemplate template) {
        timer = new Timer();
        timerTask = new SendTimeTask(template, maxSeconds, destination);
        timer.schedule(timerTask, (long) delay * MILLI, MILLI);
    }

}