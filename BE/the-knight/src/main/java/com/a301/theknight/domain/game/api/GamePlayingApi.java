package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.doubt.request.GameDoubtRequest;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtPassResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.service.GameAttackDefenseService;
import com.a301.theknight.domain.game.service.GameDoubtService;
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
    private final SendMessageService messageService;

    // AttackApi 3개
    @MessageMapping(value = "/games/{gameId}/attack")
    public void attack(@Min(1) @DestinationVariable long gameId, @Valid GameAttackRequest gameAttackRequest, @LoginMemberId long memberId) {
        gameAttackDefenseService.attack(gameId, memberId, gameAttackRequest);

        messageService.convertCall(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/attack-pass")
    public void attackPass(@Min(1) @DestinationVariable long gameId,  @LoginMemberId long memberId) {
        gameAttackDefenseService.isAttackPass(gameId, memberId);

        messageService.convertCall(gameId);
    }

    // DefenseApi 3개
    @MessageMapping(value = "/games/{gameId}/defense")
    public void defense(@Min(1) @DestinationVariable long gameId, @Valid GameDefenseRequest gameDefenseRequest, @LoginMemberId long memberId) {
        gameAttackDefenseService.defense(gameId, memberId, gameDefenseRequest);

        messageService.convertCall(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/defense-pass")
    public void defensePass(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId) {
        gameAttackDefenseService.isDefensePass(gameId, memberId);

        messageService.convertCall(gameId);
    }

    // DoubtApi 3개
    @MessageMapping(value = "/games/{gameId}/doubt")
    public void doubt(@Min(1) @DestinationVariable long gameId, @Valid GameDoubtRequest doubtRequest, @LoginMemberId long memberId) {
        GameStatus curStatus = doubtRequest.getDoubtStatus();
        gameDoubtService.doubt(gameId, memberId, doubtRequest.getSuspected().getMemberId(), doubtRequest.getDoubtStatus());

        messageService.convertCall(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/doubt-pass")
    public void doubtPass(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId) {
        DoubtPassResponse doubtPassResponse = gameDoubtService.doubtPass(gameId, memberId);

        if(doubtPassResponse != null)
            messageService.sendData(gameId, "/doubt-pass", doubtPassResponse);
        messageService.convertCall(gameId);
    }

}
