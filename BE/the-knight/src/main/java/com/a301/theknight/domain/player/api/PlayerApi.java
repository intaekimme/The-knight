package com.a301.theknight.domain.player.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.player.dto.ReadyDto;
import com.a301.theknight.domain.player.dto.request.PlayerReadyRequest;
import com.a301.theknight.domain.player.dto.request.PlayerTeamRequest;
import com.a301.theknight.domain.player.dto.response.PlayerEntryResponse;
import com.a301.theknight.domain.player.dto.response.PlayerExitDto;
import com.a301.theknight.domain.player.dto.response.PlayerTeamResponse;
import com.a301.theknight.domain.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequiredArgsConstructor
public class PlayerApi {

    private final SendMessageService messageService;
    private final PlayerService playerService;

    @MessageMapping(value="/games/{gameId}/entry")
    public void entry(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId){
        PlayerEntryResponse playerEntryResponse = playerService.entry(gameId, memberId);

        messageService.sendData(gameId,"/entry", playerEntryResponse);

        messageService.sendDataToServer(gameId, "/members");
    }

    @MessageMapping(value="/games/{gameId}/exit")
    public void exit(@Min(1) @DestinationVariable long gameId,
                     @LoginMemberId long memberId){
        PlayerExitDto exitDto = playerService.exit(gameId, memberId);
        if (exitDto.isLeaderExited()) {
            //TODO: /delete에 memberId 바인딩 여부 테스트
            messageService.sendDataToServer(gameId, "/delete", memberId);
            return;
        }
        messageService.sendData(gameId, "/exit", exitDto.getPlayerExitResponse());
    }

    @MessageMapping(value="/games/{gameId}/team")
    public void team(@Min(1) @DestinationVariable long gameId, @Valid PlayerTeamRequest playerTeamMessage,
                     @LoginMemberId long memberId){
        PlayerTeamResponse playerTeamResponse = playerService.team(gameId, memberId, playerTeamMessage);
        messageService.sendData(gameId, "/team", playerTeamResponse);
    }

    @MessageMapping(value="/games/{gameId}/ready")
    public void ready(@Min(1) @DestinationVariable long gameId, @LoginMemberId long memberId,
                      @Valid PlayerReadyRequest playerReadyMessage){
        ReadyDto readyDto = playerService.ready(gameId, memberId, playerReadyMessage);

        if (readyDto.isCanStart()) {
            messageService.convertCall(gameId);
            return;
        }
        messageService.sendData(gameId, "/ready", readyDto);
    }

}
