package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.doubt.request.GameDoubtRequest;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtPassDto;
import com.a301.theknight.domain.game.dto.prepare.response.SelectCompleteDto;
import com.a301.theknight.domain.game.dto.prepare.response.SelectResponse;
import com.a301.theknight.domain.game.service.GameAttackDefenseService;
import com.a301.theknight.domain.game.service.GameDoubtService;
import com.a301.theknight.domain.game.service.GamePrepareService;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.aop.annotation.PreventClick;
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
    @PreventClick
    @MessageMapping(value = "/games/{gameId}/attack")
    public void attack(@Min(1) @DestinationVariable long gameId, @Valid GameAttackRequest gameAttackRequest, @LoginMemberId long memberId) {
        gameAttackDefenseService.attack(gameId, memberId, gameAttackRequest);

        messageService.convertCall(gameId);
    }

    @PreventClick
    @MessageMapping(value = "/games/{gameId}/attack-pass")
    public void attackPass(@Min(1) @DestinationVariable long gameId,  @LoginMemberId long memberId) {
        gameAttackDefenseService.checkAttackPass(gameId, memberId);

        messageService.convertCall(gameId);
    }

    // DefenseApi 3개
    @PreventClick
    @MessageMapping(value = "/games/{gameId}/defense")
    public void defense(@Min(1) @DestinationVariable long gameId, @Valid GameDefenseRequest gameDefenseRequest, @LoginMemberId long memberId) {
        gameAttackDefenseService.defense(gameId, memberId, gameDefenseRequest);

        messageService.convertCall(gameId);
    }

    @PreventClick
    @MessageMapping(value = "/games/{gameId}/defense-pass")
    public void defensePass(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId) {
        gameAttackDefenseService.isDefensePass(gameId, memberId);

        messageService.convertCall(gameId);
    }

    // DoubtApi 3개
    @PreventClick
    @MessageMapping(value = "/games/{gameId}/doubt")
    public void doubt(@Min(1) @DestinationVariable long gameId, @Valid GameDoubtRequest doubtRequest, @LoginMemberId long memberId) {
        gameDoubtService.doubt(gameId, memberId, doubtRequest.getSuspected().getMemberId(), doubtRequest.getDoubtStatus());

        messageService.convertCall(gameId);
    }

    @MessageMapping(value = "/games/{gameId}/doubt-pass")
    @PreventClick
    public void doubtPass(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId) {
        DoubtPassDto doubtPassDto = gameDoubtService.doubtPass(gameId, memberId);

        if (doubtPassDto.isFullCount()) {
            messageService.convertCall(gameId);
        } else {
            messageService.sendData(gameId, "/doubt-pass", doubtPassDto.getDoubtPassResponse());
        }
    }

}
