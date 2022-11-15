package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponse;
import com.a301.theknight.domain.game.dto.player.response.MemberTeamResponse;
import com.a301.theknight.domain.game.entity.Weapon;
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
public class DefenseDoubtDataService extends GameDataService {

    private final GameRedisRepository redisRepository;

    public DefenseDoubtDataService(RedissonClient redissonClient, GameRedisRepository redisRepository) {
        super(redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void makeData(long gameId) {
    }

    @Override
    public void sendScreenData(long gameId, SendMessageService messageService) {
        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        long defenderId = turn.getDefenderId();

        DefenseResponse response = DefenseResponse.builder()
                .defender(MemberTeamResponse.builder()
                        .memberId(defenderId)
                        .team(getInGamePlayer(gameId, defenderId).getTeam().name())
                        .build())
                .weapon(Weapon.SHIELD.name())
                .hand(turn.getDefendData().getDefendHand().name())
                .build();
        messageService.sendData(gameId, "/defense-info", response);
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
