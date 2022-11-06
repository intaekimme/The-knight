package com.a301.theknight.domain.game.service;


import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
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

    public void defense(long gameId, long memberId, GameDefenseRequest gameDefenseRequest){
        checkPlayerId(memberId, gameDefenseRequest.getDefender().getId());
        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        InGamePlayer defender = getInGamePlayer(gameId,  gameDefenseRequest.getDefender().getId());
        turn.recordDefenseTurn(defender, gameDefenseRequest);
        turn.checkLyingDefense(defender);

        findInGame.recordTurnData(turn);
        findInGame.changeStatus(GameStatus.DEFENSE_DOUBT);
        gameRedisRepository.saveInGame(gameId, findInGame);

    }

    public DefenseResponse getDefenseInfo(long game) {
        InGame findInGame = getInGame(game);
        TurnData turn = getTurnData(findInGame);

        return DefenseResponse.builder()
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(Weapon.SHIELD.name())
                .hand(turn.getDefendData().getDefendHand().name())
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
