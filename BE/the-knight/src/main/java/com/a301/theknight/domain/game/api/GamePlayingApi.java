package com.a301.theknight.domain.game.api;

import com.a301.theknight.domain.game.dto.playing.GameMembersInfoDto;
import com.a301.theknight.domain.game.dto.playing.GamePrepareDto;
import com.a301.theknight.domain.game.dto.playing.GameStartRequest;
import com.a301.theknight.domain.game.dto.playing.GameWeaponDto;
import com.a301.theknight.domain.game.service.GamePlayingService;
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

    @MessageMapping(value = "/pub/games/{gameId}/start")
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

    @MessageMapping(value = "/pub/games/{gameId}/members")
    public void getGamePlayerData(@DestinationVariable long gameId) {
        GameMembersInfoDto membersInfo = gamePlayingService.getMembersInfo(gameId);

        template.convertAndSend(makeDestinationUri(SEND_PREFIX, gameId, "/members"), membersInfo);
    }

    private String makeDestinationUri(String prefix, long gameId, String postfix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(gameId).append(postfix);

        return sb.toString();
    }
}
