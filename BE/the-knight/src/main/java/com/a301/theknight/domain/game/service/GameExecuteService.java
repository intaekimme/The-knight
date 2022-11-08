package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.execute.response.AttackerDto;
import com.a301.theknight.domain.game.dto.execute.response.DefenderDto;
import com.a301.theknight.domain.game.dto.execute.response.GameExecuteResponse;
import com.a301.theknight.domain.game.dto.pass.response.PassResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@RequiredArgsConstructor
@Service
public class GameExecuteService {

    private final GameRedisRepository redisRepository;

    @Transactional
    public PassResponse pass(long gameId, long memberId) {
        /*
        1. inGame State로 공격 패스인지 방어 패스인지
            -> 방어 패스 : 공격 진행으로 화면 전환 응답 -> 턴 수행으로 메시지 발행
            -> 공격 패스 : 다음 공격자로 화면 전환 응답 -> 바로 어태커로 메시지 발행
        */
        InGame inGame = getInGame(gameId);
        GameStatus status = inGame.getGameStatus();

        if (GameStatus.ATTACK.equals(status)) {
            //TODO: 어택 패스 -> 어택 정보 비워주고 다음 공격자로 다시 감.
            return null;
        }
        //TODO: 턴 수행 응답.
        return null;
    }

    @Transactional
    public GameExecuteResponse executeTurn(long gameId) {
        InGame inGame = getInGame(gameId);
        TurnData turnData = inGame.getTurnData();

        InGamePlayer defender = getInGamePlayer(gameId, turnData.getDefenderId());
        AttackData attackData = turnData.getAttackData();
        DefendData defendData = turnData.getDefendData();

        int defendCount = defendData.getShieldCount();
        boolean isDefendPass = defendData.isDefendPass();

        Weapon attackWeapon = attackData.getWeapon();
        int resultCount = defendCount - attackWeapon.getCount();

        GameStatus nextStatus = GameStatus.ATTACK;
        if (resultCount < 0 || isDefendPass) {
            defender.death();
            if (defender.isLeader()) {
                nextStatus = GameStatus.END;
            }
        } else {
            defender.changeCount(resultCount, defendData.getDefendHand());
        }

        inGame.changeStatus(nextStatus);
        redisRepository.saveInGame(gameId, inGame);
        redisRepository.saveInGamePlayer(gameId, defender.getMemberId(), defender);

        return getGameExecuteResponse(inGame, turnData, defender, resultCount);
    }

    private GameExecuteResponse getGameExecuteResponse(InGame inGame, TurnData turnData, InGamePlayer defender, int nextCount) {
        AttackData attackData = turnData.getAttackData();
        DefendData defendData = turnData.getDefendData();

        AttackerDto attackerDto = AttackerDto.builder()
                .id(turnData.getAttackerId())
                .hand(attackData.getAttackHand().name())
                .weapon(attackData.getWeapon().name())
                .build();
        DefenderDto defenderDto = DefenderDto.builder()
                .id(turnData.getDefenderId())
                .hand(defendData.getDefendHand().name())
                .isDead(defender.isDead())
                .restCount(nextCount)
                .build();

        return GameExecuteResponse.builder()
                .attackTeam(inGame.getCurrentAttackTeam().name())
                .attacker(attackerDto)
                .defender(defenderDto)
                .build();
    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private InGamePlayer getInGamePlayer(long gameId, Long memberId) {
        return redisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }
}
