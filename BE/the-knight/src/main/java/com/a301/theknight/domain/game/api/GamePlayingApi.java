package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.GameModifyRequest;
import com.a301.theknight.domain.game.dto.playing.*;
import com.a301.theknight.domain.game.service.GamePlayingService;
import com.a301.theknight.domain.game.util.GameTimer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GamePlayingApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;
    private final GamePlayingService gamePlayingService;

    @MessageMapping(value = "/games/{gameId}/start")
    public void prepareGameStart(@DestinationVariable long gameId, GameStartRequest gameStartRequest) {
        if (!gamePlayingService.canStartGame(gameId, gameStartRequest.getSetGame())) {
            return;
        }
        GamePrepareDto gamePrepareDto = gamePlayingService.prepareToStartGame(gameId);

        template.convertAndSend(makeDestinationUri(SEND_PREFIX, gameId,"/a/weapons"), gamePrepareDto.getGameWeaponDto());
        template.convertAndSend(makeDestinationUri(SEND_PREFIX, gameId,"/b/weapons"), gamePrepareDto.getGameWeaponDto());

        template.convertAndSend(makeDestinationUri(SEND_PREFIX, gameId,"/a/leader"), gamePrepareDto.getGameLeaderDto().getTeamA());
        template.convertAndSend(makeDestinationUri(SEND_PREFIX, gameId,"/b/leader"), gamePrepareDto.getGameLeaderDto().getTeamB());
        getGamePlayerData(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/members")
    public void getGamePlayerData(@DestinationVariable long gameId) {
        GameMembersInfoDto membersInfo = gamePlayingService.getMembersInfo(gameId);

        template.convertAndSend(makeDestinationUri(SEND_PREFIX, gameId, "/members"), membersInfo);
    }

    @MessageMapping(value="/games/{gameId}/timer")
    public void timer(@DestinationVariable long gameId, GameTimerDto gameTimerDto){
        GameTimer gameTimer = new GameTimer();
        gameTimer.sendSeconds(gameTimerDto.getDelay(), gameTimerDto.getSecond(),
                makeDestinationUri(SEND_PREFIX, gameId, "/timer"), template);
    }

    @MessageMapping(value="/games/{gameId}/weapons")
    public void choiceWeapon(@DestinationVariable long gameId, GameWeaponRequest gameWeaponRequest,
                             @LoginMemberId Long memberId){
//        GameWeaponDto gameWeaponDto = gamePlayingService.choiceWeapon(gameId, memberId, gameWeaponRequest);
    }

    @MessageMapping(value="/games/{gameId}/orders")
    public void choiceOrder(@DestinationVariable long gameId, GameOrderRequest gameOrderRequest){

    }

    private String makeDestinationUri(String prefix, long gameId, String postfix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(gameId).append(postfix);

        return sb.toString();
    }
}
