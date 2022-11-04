package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.game.dto.prepare.GameTimerDto;
import com.a301.theknight.domain.game.dto.prepare.request.*;
import com.a301.theknight.domain.game.dto.prepare.response.*;
import com.a301.theknight.domain.game.entity.redis.GameWeaponData;
import com.a301.theknight.domain.game.service.GamePrepareService;
import com.a301.theknight.domain.game.util.GameTimer;
import com.a301.theknight.domain.player.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GamePrepareApi {

    private static final String SEND_PREFIX = "/sub/games/";
    private final SimpMessagingTemplate template;
    private final GamePrepareService gamePrepareService;

    @MessageMapping(value = "/games/{gameId}/prepare")
    public void prepareGameStart(@DestinationVariable long gameId) {
        GamePrepareDto gamePrepareDto = gamePrepareService.prepare(gameId);

        sendWeaponResponse(gameId, Team.A, gamePrepareDto.getGameWeaponData());
        sendWeaponResponse(gameId, Team.B, gamePrepareDto.getGameWeaponData());

        template.convertAndSend(makeDestinationUri(gameId,"/a/leader"), gamePrepareDto.getGameLeaderDto().getTeamA());
        template.convertAndSend(makeDestinationUri(gameId,"/b/leader"), gamePrepareDto.getGameLeaderDto().getTeamB());
        getGamePlayerData(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/players")
    public void getGamePlayerData(@DestinationVariable long gameId) {
        GamePlayersInfoDto playersInfo = gamePrepareService.getPlayersInfo(gameId);

        template.convertAndSend(makeDestinationUri(gameId, "/players"), playersInfo);
    }

    @MessageMapping(value="/games/{gameId}/timer")
    public void timer(@DestinationVariable long gameId, GameTimerDto gameTimerDto){
        GameTimer gameTimer = new GameTimer();
        gameTimer.sendSeconds(gameTimerDto.getDelay(), gameTimerDto.getSecond(),
                makeDestinationUri(gameId, "/timer"), template);
    }

    @MessageMapping(value="/games/{gameId}/weapon-choice")
    public void choiceWeapon(@DestinationVariable long gameId, GameWeaponChoiceRequest gameWeaponChoiceRequest,
                             @LoginMemberId long memberId){
        GameWeaponResponse weaponResponse = gamePrepareService.choiceWeapon(gameId, memberId, gameWeaponChoiceRequest);

        sendWeaponResponse(gameId, weaponResponse.getTeam(), weaponResponse.getGameWeaponData());
    }

    @MessageMapping(value="/games/{gameId}/weapon-delete")
    public void deleteWeapon(@DestinationVariable long gameId, GameWeaponDeleteRequest weaponDeleteRequest,
                              @LoginMemberId long memberId){
        GameWeaponResponse weaponResponse = gamePrepareService.cancelWeapon(gameId, memberId, weaponDeleteRequest.isLeft());

        sendWeaponResponse(gameId, weaponResponse.getTeam(), weaponResponse.getGameWeaponData());
    }


    @MessageMapping(value="/games/{gameId}/orders")
    public void choiceOrder(@DestinationVariable long gameId, GameOrderRequest gameOrderRequest,
                            @LoginMemberId long memberId){
        GameOrderResponse orderResponse = gamePrepareService.choiceOrder(gameId, memberId, gameOrderRequest);

        if (orderResponse != null) {
            sendOrderResponse(gameId, orderResponse.getTeam(), orderResponse);
        }
    }

    @MessageMapping(value="/games/{gameId}/pre-attack")
    public void getPreAttack(@DestinationVariable long gameId){
        GamePreAttackResponse response = gamePrepareService.getPreAttack(gameId);
        template.convertAndSend(makeDestinationUri(gameId, "/pre-attack"), response);
    }

    @MessageMapping(value="/games/{gameId}/select-complete")
    public void completeSelect(@DestinationVariable long gameId, @LoginMemberId long memberId,
                               GameCompleteSelectRequest gameCompleteSelectRequest){
        boolean completed = gamePrepareService.completeSelect(gameId, memberId, gameCompleteSelectRequest.getTeam());
        if (completed) {
            template.convertAndSend(makeDestinationUri(gameId, "/convert"));
        }
    }

    private void sendOrderResponse(long gameId, Team team, GameOrderResponse orderResponse) {
        if (Team.A.equals(team)) {
            template.convertAndSend(makeDestinationUri(gameId,"/a/orders"), orderResponse);
            return;
        }
        template.convertAndSend(makeDestinationUri(gameId,"/b/orders"), orderResponse);
    }

    private void sendWeaponResponse(long gameId, Team team, GameWeaponData weaponData) {
        if (Team.A.equals(team)) {
            template.convertAndSend(makeDestinationUri(gameId,"/a/weapons"), weaponData);
            return;
        }
        template.convertAndSend(makeDestinationUri(gameId,"/b/weapons"), weaponData);
    }

    private String makeDestinationUri(long gameId, String postfix) {
        return SEND_PREFIX + gameId + postfix;
    }
}
