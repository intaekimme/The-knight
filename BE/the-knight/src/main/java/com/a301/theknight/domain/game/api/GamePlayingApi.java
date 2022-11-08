package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackPassRequest;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.attack.response.AttackResponse;
import com.a301.theknight.domain.game.dto.attacker.AttackerDto;
import com.a301.theknight.domain.game.dto.defense.request.GameDefensePassRequest;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponse;
import com.a301.theknight.domain.game.dto.doubt.request.GameDoubtRequest;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtResponse;
import com.a301.theknight.domain.game.dto.execute.response.GameExecuteResponse;
import com.a301.theknight.domain.game.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RequiredArgsConstructor
@Controller
public class GamePlayingApi {
    private final GameAttackService gameAttackService;
    private final GameAttackerService gameAttackerService;
    private final GameDefenseService gameDefenseService;
    private final GameDoubtService gameDoubtService;
    private final GameExecuteService gameExecuteService;

    private final SendMessageService messageService;


    // AttackApi 3개
    @MessageMapping(value = "/games/{gameId}/attack")
    public void attack(@Min(1) @DestinationVariable long gameId, @Valid GameAttackRequest gameAttackRequest, @LoginMemberId long memberId) {
        gameAttackService.attack(gameId, memberId, gameAttackRequest);

        messageService.convertCall(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/attack-info")
    public void attackInfo(@Min(1) @DestinationVariable long gameId) {
        AttackResponse response = gameAttackService.getAttackInfo(gameId);
        messageService.sendData(gameId, "/attack-info", response);

        messageService.proceedCall(gameId, 500);
    }

    @MessageMapping(value = "/games/{gameId}/attack-pass")
    public void attackPass(@Min(1) @DestinationVariable long gameId, @Valid GameAttackPassRequest gameAttackPassRequest, @LoginMemberId long memberId) {
        gameAttackService.isAttackPass(gameId, gameAttackPassRequest, memberId);

        messageService.convertCall(gameId);
    }

    // AttackerApi 1개
    @MessageMapping(value = "/games/{gameId}/attacker")
    public void getAttacker(@Min(1) @DestinationVariable long gameId) {

        AttackerDto attackerDto = gameAttackerService.getAttacker(gameId);

        messageService.sendData(gameId, "/a/attacker", attackerDto.getAttackerResponseA());
        messageService.sendData(gameId, "/b/attacker", attackerDto.getAttackerResponseB());

        messageService.proceedCall(gameId, 500);
    }


    // DefenseApi 3개
    @MessageMapping(value = "/games/{gameId}/defense")
    public void defense(@Min(1) @DestinationVariable long gameId, @Valid GameDefenseRequest gameDefenseRequest, @LoginMemberId long memberId) {
        gameDefenseService.defense(gameId, memberId, gameDefenseRequest);

        messageService.convertCall(gameId);

    }

    @MessageMapping(value = "/games/{gameId}/defense-info")
    public void defendInfo(@Min(1) @DestinationVariable long gameId) {
        DefenseResponse response = gameDefenseService.getDefenseInfo(gameId);
        messageService.sendData(gameId, "/defense-info", response);

        messageService.proceedCall(gameId, 500);
    }

    @MessageMapping(value = "/games/{gameId}/defense-pass")
    public void defensePass(@Min(1) @DestinationVariable long gameId, @Valid GameDefensePassRequest gameDefensePassRequest, @LoginMemberId long memberId) {
        gameDefenseService.isDefensePass(gameId, gameDefensePassRequest, memberId);

        messageService.convertCall(gameId);
    }

    // DoubtApi 3개
    @MessageMapping(value = "/games/{gameId}/doubt")
    public void doubt(@Min(1) @DestinationVariable long gameId, @Valid GameDoubtRequest doubtRequest, @LoginMemberId long memberId) {
        gameDoubtService.doubt(gameId, memberId, doubtRequest.getSuspected().getId(), doubtRequest.getDoubtStatus());

        messageService.convertCall(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/doubt-info")
    public void doubtInfo(@Min(1) @DestinationVariable long gameId) throws InterruptedException {
        DoubtResponse doubtResponse = gameDoubtService.getDoubtInfo(gameId);
        messageService.sendData(gameId, "/doubt-info", doubtResponse);

        messageService.proceedCall(gameId, 500);

        Thread.sleep(10000);
        messageService.convertCall(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/doubt-pass")
    public void doubtPass(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId) {
        gameDoubtService.doubtPass(gameId, memberId);

        messageService.convertCall(gameId);
    }

    // ExecuteApi 1개
    @MessageMapping(value = "/games/{gameId}/execute")
    public void executeTurn(@Min(1) @DestinationVariable long gameId) throws InterruptedException {
        GameExecuteResponse executeResponse = gameExecuteService.executeTurn(gameId);

        messageService.sendData(gameId, "/execute", executeResponse);

        messageService.proceedCall(gameId, 500);

        Thread.sleep(5000);
        messageService.convertCall(gameId);
    }
}
