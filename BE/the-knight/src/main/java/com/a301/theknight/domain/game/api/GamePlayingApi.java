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
import com.a301.theknight.domain.game.dto.doubt.response.DoubtResponseDto;
import com.a301.theknight.domain.game.dto.execute.response.GameExecuteResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GamePreAttackResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.service.GameAttackDefenseService;
import com.a301.theknight.domain.game.service.GameDoubtService;
import com.a301.theknight.domain.game.service.GameExecuteEndService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RequiredArgsConstructor
@Controller
public class GamePlayingApi {
    private final GameAttackDefenseService gameAttackDefenseService;

    private final GameDoubtService gameDoubtService;
    private final GameExecuteEndService gameExecuteEndService;
    private final SendMessageService messageService;

    @MessageMapping(value="/games/{gameId}/pre-attack")
    public void getPreAttack(@Min(1) @DestinationVariable long gameId) throws InterruptedException {
        GamePreAttackResponse response = gameAttackDefenseService.getPreAttack(gameId);
        messageService.sendData(gameId, "/pre-attack", response);

        messageService.proceedCall(gameId, 500);

        messageService.convertCall(gameId, 3000);
    }

    // AttackerApi 1개
    @MessageMapping(value = "/games/{gameId}/attacker")
    public void getAttacker(@Min(1) @DestinationVariable long gameId) {

        AttackerDto attackerDto = gameAttackDefenseService.getAttacker(gameId);

        messageService.sendData(gameId, "/a/attacker", attackerDto.getAttackerResponseA());
        messageService.sendData(gameId, "/b/attacker", attackerDto.getAttackerResponseB());

        messageService.proceedCall(gameId, 500);
    }

    // AttackApi 3개
    @MessageMapping(value = "/games/{gameId}/attack")
    public void attack(@Min(1) @DestinationVariable long gameId, @Valid GameAttackRequest gameAttackRequest, @LoginMemberId long memberId) {
        gameAttackDefenseService.attack(gameId, memberId, gameAttackRequest);

        messageService.convertCall(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/attack-info")
    public void attackInfo(@Min(1) @DestinationVariable long gameId) {
        AttackResponse response = gameAttackDefenseService.getAttackInfo(gameId);
        messageService.sendData(gameId, "/attack-info", response);

        messageService.proceedCall(gameId, 500);
    }

    @MessageMapping(value = "/games/{gameId}/attack-pass")
    public void attackPass(@Min(1) @DestinationVariable long gameId, @Valid GameAttackPassRequest gameAttackPassRequest, @LoginMemberId long memberId) {
        gameAttackDefenseService.isAttackPass(gameId, gameAttackPassRequest, memberId);

        messageService.convertCall(gameId);
    }

    // DefenseApi 3개
    @MessageMapping(value = "/games/{gameId}/defense")
    public void defense(@Min(1) @DestinationVariable long gameId, @Valid GameDefenseRequest gameDefenseRequest, @LoginMemberId long memberId) {
        gameAttackDefenseService.defense(gameId, memberId, gameDefenseRequest);

        messageService.convertCall(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/defense-info")
    public void defendInfo(@Min(1) @DestinationVariable long gameId) {
        DefenseResponse response = gameAttackDefenseService.getDefenseInfo(gameId);
        messageService.sendData(gameId, "/defense-info", response);

        messageService.proceedCall(gameId, 500);
    }

    @MessageMapping(value = "/games/{gameId}/defense-pass")
    public void defensePass(@Min(1) @DestinationVariable long gameId, @Valid GameDefensePassRequest gameDefensePassRequest, @LoginMemberId long memberId) {
        gameAttackDefenseService.isDefensePass(gameId, gameDefensePassRequest, memberId);

        messageService.convertCall(gameId);
    }

    // DoubtApi 3개
    @MessageMapping(value = "/games/{gameId}/doubt")
    public void doubt(@Min(1) @DestinationVariable long gameId, @Valid GameDoubtRequest doubtRequest, @LoginMemberId long memberId) {
        GameStatus curStatus = doubtRequest.getDoubtStatus();
        gameDoubtService.doubt(gameId, memberId, doubtRequest.getSuspected().getId(), doubtRequest.getDoubtStatus());

        messageService.convertCall(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/doubt-info")
    public void doubtInfo(@Min(1) @DestinationVariable long gameId) throws InterruptedException {
        DoubtResponseDto doubtDto = gameDoubtService.getDoubtInfo(gameId);

        messageService.sendData(gameId, "/doubt-info", doubtDto.getDoubtResponse());
        messageService.proceedCall(gameId, 500);

        messageService.convertCall(gameId, 3000);
    }

    @MessageMapping(value = "/games/{gameId}/doubt-pass")
    public void doubtPass(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId) {
        gameDoubtService.doubtPass(gameId, memberId);

        messageService.convertCall(gameId);
    }

    // ExecuteApi 1개
    @MessageMapping(value = "/games/{gameId}/execute")
    public void executeTurn(@Min(1) @DestinationVariable long gameId) throws InterruptedException {
        GameExecuteResponse executeResponse = gameExecuteEndService.executeTurn(gameId);

        messageService.sendData(gameId, "/execute", executeResponse);
        messageService.proceedCall(gameId, 500);

        messageService.convertCall(gameId, 5000);
    }
}
