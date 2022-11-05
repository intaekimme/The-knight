package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.pass.response.PassResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@RequiredArgsConstructor
@Service
public class GamePassService {

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
    public void turnExecute(long gameId) {
        TurnData turnData = getInGame(gameId).getTurnData();

        InGamePlayer attacker = getInGamePlayer(gameId, turnData.getAttackerId());
        InGamePlayer defender = getInGamePlayer(gameId, turnData.getAttackerId());

        AttackData attackData = turnData.getAttackData();
        DefendData defendData = turnData.getDefendData();

        int defendCount = Hand.LEFT.equals(defendData.getDefendHand())
                ? defender.getLeftCount() : defender.getRightCount();
        Weapon attackWeapon = Hand.LEFT.equals(attackData.getAttackHand())
                ? attacker.getLeftWeapon() : attacker.getRightWeapon();

        int resultCount = defendCount - attackWeapon.getCount();
        if (resultCount < 0) {
            defender.death();
            //TODO: 디펜더가 리더인 경우 -> 게임 종료
        } else {
            defender.changeCount(resultCount, defendData.getDefendHand());
        }

        //TODO: 턴 수행 후 응답은?? -> 다음 공격자로??
    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomException(INGAME_IS_NOT_EXIST));
    }

    private InGamePlayer getInGamePlayer(long gameId, Long memberId) {
        return redisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomException(INGAME_PLAYER_IS_NOT_EXIST));
    }
}
