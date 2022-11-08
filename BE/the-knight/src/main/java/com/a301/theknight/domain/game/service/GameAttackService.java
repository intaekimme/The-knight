package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackPassRequest;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.attack.response.AttackResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GamePreAttackResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GameAttackService {

    private final GameRedisRepository gameRedisRepository;

    @Transactional
    public GamePreAttackResponse getPreAttack(long gameId) {
        InGame inGame = getInGame(gameId);
        Team preAttackTeam = inGame.getCurrentAttackTeam();
        //TODO: 반대 팀으로 변경 코드 넣기 (이후 공격자 조회 시 팀을 바꾸면서 조회하기 때문)
        inGame.changeStatus(GameStatus.ATTACK);

        return new GamePreAttackResponse(preAttackTeam);
    }

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
    public void isAttackPass(long gameId, GameAttackPassRequest gameAttackPassRequest, long memberId) {
        checkPlayerId(memberId, gameAttackPassRequest.getAttacker().getId());
        InGame findInGame = getInGame(gameId);

        if(findInGame.getGameStatus().equals(GameStatus.ATTACK)) return;
        throw new CustomWebSocketException(UNABLE_TO_PASS_ATTACK);
    }

    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private InGamePlayer getInGamePlayer(long gameId, long memberId) {
        return gameRedisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private TurnData getTurnData(InGame inGame){
       if(inGame.isTurnDataEmpty()){
           inGame.initTurnData();
       }
        return inGame.getTurnData();
    }

    private void checkPlayerId(long memberId, long playerId){
        if (memberId != playerId) {
            throw new CustomWebSocketException(PLAYER_IS_NOT_USER_WHO_LOGGED_IN);
        }
    }


}
