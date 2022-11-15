package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.attack.response.AttackResponse;
import com.a301.theknight.domain.game.dto.player.response.MemberTeamResponse;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@Service
public class AttackDoubtDataService extends GameDataService {

    private final GameRedisRepository redisRepository;

    public AttackDoubtDataService(RedissonClient redissonClient, GameRedisRepository redisRepository) {
        super(redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void makeData(long gameId) {
    }

    @Override
    public void sendScreenData(long gameId, SendMessageService messageService) {
        InGame inGame = getInGame(gameId);
        TurnData turn = getTurnData(inGame);

        long attackerId = turn.getAttackerId();
        long defenderId = turn.getDefenderId();

        AttackResponse response = AttackResponse.builder()
                .attacker(MemberTeamResponse.builder()
                        .memberId(attackerId)
                        .team(getInGamePlayer(gameId, attackerId).getTeam().name())
                        .build())
                .defender(MemberTeamResponse.builder()
                        .memberId(defenderId)
                        .team(getInGamePlayer(gameId, defenderId).getTeam().name())
                        .build())
                .weapon(turn.getAttackData().getWeapon().name())
                .hand(turn.getAttackData().getAttackHand().name())
                .build();
        messageService.sendData(gameId, "/attack-info", response);
    }

    private TurnData getTurnData(InGame inGame) {
        if (inGame.isTurnDataEmpty()) {
            inGame.initTurnData();
        }
        return inGame.getTurnData();
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
