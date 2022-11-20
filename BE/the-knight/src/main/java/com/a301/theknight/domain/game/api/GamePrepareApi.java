package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.prepare.GamePlayersInfoResponse;
import com.a301.theknight.domain.game.dto.prepare.request.GameOrderRequest;
import com.a301.theknight.domain.game.dto.prepare.request.GameWeaponChoiceRequest;
import com.a301.theknight.domain.game.dto.prepare.request.GameWeaponDeleteRequest;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GameWeaponResponse;
import com.a301.theknight.domain.game.dto.prepare.response.SelectCompleteDto;
import com.a301.theknight.domain.game.dto.prepare.response.SelectResponse;
import com.a301.theknight.domain.game.entity.redis.GameWeaponData;
import com.a301.theknight.domain.game.service.GamePrepareService;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.aop.annotation.PreventClick;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.Min;

@Controller
@RequiredArgsConstructor
public class GamePrepareApi {

    private final GamePrepareService gamePrepareService;

    private final SendMessageService messageService;

    @MessageMapping(value = "/games/{gameId}/players")
    public void getGamePlayerData(@DestinationVariable @Min(1) long gameId) {
        GamePlayersInfoResponse playersInfo = gamePrepareService.getTeamPlayersInfo(gameId);

        messageService.sendData(gameId, "/a/players", playersInfo.getPlayersAInfoDto());
        messageService.sendData(gameId, "/b/players", playersInfo.getPlayersBInfoDto());
    }

    @MessageMapping(value="/games/{gameId}/weapon-choice")
    public void choiceWeapon(@DestinationVariable @Min(1) long gameId, GameWeaponChoiceRequest gameWeaponChoiceRequest,
                             @LoginMemberId long memberId){
        GameWeaponResponse weaponResponse = gamePrepareService.choiceWeapon(gameId, memberId, gameWeaponChoiceRequest);

        sendWeaponResponse(gameId, weaponResponse.getTeam(), weaponResponse.getGameWeaponData());
        getGamePlayerData(gameId);
    }

    @MessageMapping(value="/games/{gameId}/weapon-delete")
    public void deleteWeapon(@DestinationVariable @Min(1) long gameId, GameWeaponDeleteRequest weaponDeleteRequest,
                              @LoginMemberId long memberId){
        GameWeaponResponse weaponResponse = gamePrepareService.cancelWeapon(gameId, memberId, weaponDeleteRequest.getDeleteHand());

        sendWeaponResponse(gameId, weaponResponse.getTeam(), weaponResponse.getGameWeaponData());
        getGamePlayerData(gameId);
    }

    @MessageMapping(value="/games/{gameId}/{team}/orders")
    public void choiceOrder(@DestinationVariable @Min(1) long gameId, @DestinationVariable String team,
                             GameOrderRequest gameOrderRequest, @LoginMemberId long memberId){
        Team requestTeam = getTeam(team);
        if (requestTeam == null) {
            return;
        }

        GameOrderResponse orderResponse = gamePrepareService.saveOrder(gameId, memberId, requestTeam, gameOrderRequest);
        if (orderResponse != null) {
            sendOrderResponse(gameId, requestTeam, orderResponse);
        }
    }

    @PreventClick
    @MessageMapping(value="/games/{gameId}/select-complete")
    public void completeSelect(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId){
        SelectCompleteDto selectCompleteDto = gamePrepareService.completeSelect(gameId, memberId);
        if (selectCompleteDto.isCompleted()) {
            messageService.convertCall(gameId);
            return;
        }

        String postfix = Team.A.equals(selectCompleteDto.getSelectTeam()) ? "/a/select-complete" : "/b/select-complete";
        messageService.sendData(gameId, postfix, new SelectResponse(true));
    }

    private Team getTeam(String team) {
        switch (team) {
            case "a":
                return Team.A;
            case "b":
                return Team.B;
        }
        return null;
    }

    private void sendWeaponResponse(long gameId, Team team, GameWeaponData gameWeaponData) {
        String postfix = Team.A.equals(team) ? "/a/weapons" : "/b/weapons";
        messageService.sendData(gameId, postfix, gameWeaponData);
    }

    private void sendOrderResponse(long gameId, Team team, GameOrderResponse orderResponse) {
        String postfix = Team.A.equals(team) ? "/a/orders" : "/b/orders";
        messageService.sendData(gameId, postfix, orderResponse);
    }
}
