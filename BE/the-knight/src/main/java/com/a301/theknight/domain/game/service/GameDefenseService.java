package com.a301.theknight.domain.game.service;


import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponseDto;
import com.a301.theknight.domain.game.dto.defense.response.DefenseTeamResponse;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.Hand;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GameDefenseService {

    private final GameRedisRepository gameRedisRepository;

    public DefenseResponseDto defense(long gameId, long memberId, GameDefenseRequest gameDefenseRequest){
        checkPlayerId(memberId, gameDefenseRequest.getDefender().getId());
        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        InGamePlayer defender = getInGamePlayer(gameId,  gameDefenseRequest.getDefender().getId());
        turn.recordDefenseTurn(defender, gameDefenseRequest);
        turn.checkLyingDefense(defender);

        findInGame.recordTurnData(turn);
        gameRedisRepository.saveInGame(gameId, findInGame);

        DefenseTeamResponse allayResponse = DefenseTeamResponse.builder()
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(Weapon.SHIELD.name())
                .hand(turn.getDefendData().getDefendHand().name())
                .team(defender.getTeam().name())
                .build();

        //  TODO 방어시 상대 팀에 방패와 손 정보를 전달할 것인지 논의
        DefenseTeamResponse oppResponse = DefenseTeamResponse.builder()
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(Weapon.HIDE.name())
                .hand(Hand.HIDE.name())
                .team(defender.getTeam().name().equals("A") ? "B" : "A")
                .build();

        return DefenseResponseDto.builder()
                .allyResponse(allayResponse)
                .oppResponse(oppResponse)
                .build();
    }


    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomException(INGAME_IS_NOT_EXIST));
    }


    private InGamePlayer getInGamePlayer(long gameId, long memberId) {
        return gameRedisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private TurnData getTurnData(InGame inGame){
        if(inGame.isTurnDataEmpty()){
            inGame.initTurnData();
        }
        return inGame.getTurnData();
    }

    private void checkPlayerId(long memberId, long playerId){
        if (memberId != playerId) {
            throw new CustomException(PLAYER_IS_NOT_USER_WHO_LOGGED_IN);
        }
    }
}
