package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.attack.response.AttackResponseDto;
import com.a301.theknight.domain.game.dto.attack.response.AttackTeamResponse;
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
public class GameAttackService {

    private final GameRedisRepository gameRedisRepository;

    public AttackResponseDto attack(long gameId, long memberId, GameAttackRequest gameAttackRequest){
        checkPlayerId(memberId, gameAttackRequest.getAttacker().getId());
        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        InGamePlayer attacker = getInGamePlayer(gameId, gameAttackRequest.getAttacker().getId());
        InGamePlayer defender = getInGamePlayer(gameId, gameAttackRequest.getDefender().getId());
        turn.recordAttackTurn(attacker, defender, gameAttackRequest);
        turn.checkLyingAttack(attacker);

        findInGame.recordTurnData(turn);
        gameRedisRepository.saveInGame(gameId, findInGame);

        AttackTeamResponse allyResponse = AttackTeamResponse.builder()
                .attacker(new AttackPlayerDto(turn.getAttackerId()))
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(turn.getAttackData().getWeapon().name())
                .hand(turn.getAttackData().getAttackHand().name())
                .team(attacker.getTeam().name())
                .build();

        AttackTeamResponse oppResponse = AttackTeamResponse.builder()
                .attacker(new AttackPlayerDto(turn.getAttackerId()))
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(Weapon.HIDE.name())
                .hand(Hand.HIDE.name())
                .team(attacker.getTeam().name().equals("A") ? "B" : "A")
                .build();

        return AttackResponseDto.builder()
                .allyResponse(allyResponse)
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
