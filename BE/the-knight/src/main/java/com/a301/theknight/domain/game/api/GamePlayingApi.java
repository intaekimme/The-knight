package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.playing.*;
import com.a301.theknight.domain.game.dto.playing.request.GameOrderRequest;
import com.a301.theknight.domain.game.dto.playing.request.GameStartRequest;
import com.a301.theknight.domain.game.dto.playing.request.GameWeaponChoiceRequest;
import com.a301.theknight.domain.game.dto.playing.request.GameWeaponDeleteRequest;
import com.a301.theknight.domain.game.dto.playing.response.GameMembersInfoDto;
import com.a301.theknight.domain.game.dto.playing.response.GameOrderResponse;
import com.a301.theknight.domain.game.dto.playing.response.GamePrepareDto;
import com.a301.theknight.domain.game.dto.playing.response.GameWeaponResponse;
import com.a301.theknight.domain.game.service.GamePlayingService;
import com.a301.theknight.domain.game.util.GameTimer;
import com.a301.theknight.domain.player.entity.Team;
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

        sendWeaponResponse(gameId, Team.A, gamePrepareDto.getGameWeaponData());
        sendWeaponResponse(gameId, Team.B, gamePrepareDto.getGameWeaponData());

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

    @MessageMapping(value="/games/{gameId}/weapon-choice")
    public void choiceWeapon(@DestinationVariable long gameId, GameWeaponChoiceRequest gameWeaponChoiceRequest,
                             @LoginMemberId long memberId){
        GameWeaponResponse weaponResponse = gamePlayingService.choiceWeapon(gameId, memberId, gameWeaponChoiceRequest);

        sendWeaponResponse(gameId, weaponResponse.getTeam(), weaponResponse.getGameWeaponData());
    }

    @MessageMapping(value="/games/{gameId}/weapon-delete")
    public void deleteWeapon(@DestinationVariable long gameId, GameWeaponDeleteRequest weaponDeleteRequest,
                              @LoginMemberId long memberId){
        GameWeaponResponse weaponResponse = gamePlayingService.deleteWeapon(gameId, memberId, weaponDeleteRequest.isLeft());

        sendWeaponResponse(gameId, weaponResponse.getTeam(), weaponResponse.getGameWeaponData());
    }


    @MessageMapping(value="/games/{gameId}/orders")
    public void choiceOrder(@DestinationVariable long gameId, GameOrderRequest gameOrderRequest,
                            @LoginMemberId long memberId){
        GameOrderResponse orderResponse = gamePlayingService.choiceOrder(gameId, memberId, gameOrderRequest);

        if (orderResponse != null) {
            sendOrderResponse(gameId, orderResponse.getTeam(), orderResponse);
        }
    }

    private void sendOrderResponse(long gameId, Team team, GameOrderResponse orderResponse) {
        if (Team.A.equals(team)) {
            template.convertAndSend(makeDestinationUri(SEND_PREFIX, gameId,"/a/orders"), orderResponse);
            return;
        }
        template.convertAndSend(makeDestinationUri(SEND_PREFIX, gameId,"/b/orders"), orderResponse);
    }

    private void sendWeaponResponse(long gameId, Team team, GameWeaponData weaponData) {
        if (Team.A.equals(team)) {
            template.convertAndSend(makeDestinationUri(SEND_PREFIX, gameId,"/a/weapons"), weaponData);
            return;
        }
        template.convertAndSend(makeDestinationUri(SEND_PREFIX, gameId,"/b/weapons"), weaponData);
    }

    private String makeDestinationUri(String prefix, long gameId, String postfix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(gameId).append(postfix);

        return sb.toString();
    }
}
