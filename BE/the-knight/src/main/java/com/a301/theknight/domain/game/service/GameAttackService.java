package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackPassRequest;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.attack.response.AttackResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GameAttackService {

    private final GameRedisRepository gameRedisRepository;

    @Transactional
    public void attack(long gameId, long memberId, GameAttackRequest gameAttackRequest){
        checkPlayerId(memberId, gameAttackRequest.getAttacker().getId());
        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        InGamePlayer attacker = getInGamePlayer(gameId, gameAttackRequest.getAttacker().getId());
        InGamePlayer defender = getInGamePlayer(gameId, gameAttackRequest.getDefender().getId());
        turn.recordAttackTurn(attacker, defender, gameAttackRequest);
        turn.checkLyingAttack(attacker);

        findInGame.recordTurnData(turn);
        findInGame.changeStatus(GameStatus.ATTACK_DOUBT);
        gameRedisRepository.saveInGame(gameId, findInGame);
    }

   @Transactional
    public AttackResponse getAttackInfo(long gameId) {

        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        return AttackResponse.builder()
                .attacker(new AttackPlayerDto(turn.getAttackerId()))
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(turn.getAttackData().getWeapon().name())
                .hand(turn.getAttackData().getAttackHand().name())
                .build();

    }

    @Transactional
    public boolean isAttackPass(long gameId, GameAttackPassRequest gameAttackPassRequest, long memberId) {
        checkPlayerId(memberId, gameAttackPassRequest.getAttacker().getId());
        InGame findInGame = getInGame(gameId);

        return findInGame.getGameStatus().equals(GameStatus.ATTACK);
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
