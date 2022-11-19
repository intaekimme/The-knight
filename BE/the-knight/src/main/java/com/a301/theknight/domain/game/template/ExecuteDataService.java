package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.execute.response.AttackerDto;
import com.a301.theknight.domain.game.dto.execute.response.DefenderDto;
import com.a301.theknight.domain.game.dto.execute.response.GameExecuteResponse;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.util.GameLockUtil;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@Service
public class ExecuteDataService extends GameDataService {

    private final GameRedisRepository redisRepository;

    public ExecuteDataService(GameLockUtil gameLockUtil, GameRedisRepository redisRepository) {
        super(gameLockUtil, redisRepository);
        this.redisRepository = redisRepository;
    }

    @Override
    @Transactional
    public void makeAndSendData(long gameId, SendMessageService messageService) {
        InGame inGame = getInGame(gameId);
        TurnData turnData = inGame.getTurnData();

        InGamePlayer defender = getInGamePlayer(gameId, turnData.getDefenderId());
        AttackData attackData = turnData.getAttackData();
        DefendData defendData = turnData.getDefenseData();

        int defendCount = defendData.getShieldCount();
        boolean isDefendPass = defendData.isDefendPass();

        Weapon attackWeapon = attackData.getWeapon();
        int resultCount = defendCount + attackWeapon.getCount();
        if (resultCount > 3 || isDefendPass) {
            defender.death();
        } else {
            defender.changeCount(resultCount, defendData.getDefendHand());
        }

        inGame.addTurn();
        redisRepository.saveInGame(gameId, inGame);
        redisRepository.saveInGamePlayer(gameId, defender.getMemberId(), defender);

        GameExecuteResponse response = getGameExecuteResponse(inGame, defender, resultCount);
        messageService.sendData(gameId, "/execute", response);
    }

    private GameExecuteResponse getGameExecuteResponse(InGame inGame, InGamePlayer defender, int nextCount) {
        TurnData turnData = inGame.getTurnData();
        AttackData attackData = turnData.getAttackData();
        DefendData defendData = turnData.getDefenseData();

        AttackerDto attackerDto = AttackerDto.builder()
                .memberId(turnData.getAttackerId())
                .hand(attackData.getAttackHand())
                .weapon(attackData.getWeapon())
                .build();
        DefenderDto defenderDto = DefenderDto.builder()
                .memberId(turnData.getDefenderId())
                .hand(defendData.getDefendHand())
                .isDead(defender.isDead())
                .hitCount(nextCount)
                .build();

        return GameExecuteResponse.builder()
                .attackTeam(inGame.getCurrentAttackTeam().name())
                .attacker(attackerDto)
                .defender(defenderDto)
                .build();
    }

    private InGamePlayer getInGamePlayer(long gameId, long memberId) {
        return redisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }
}
