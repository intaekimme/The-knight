package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponse;
import com.a301.theknight.domain.game.dto.player.response.MemberTeamResponse;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.util.GameLockUtil;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@Service
public class DefenseDoubtDataService extends GameDataService {

    private final GameRedisRepository redisRepository;

    public DefenseDoubtDataService(GameLockUtil gameLockUtil, GameRedisRepository redisRepository) {
        super(gameLockUtil, redisRepository);
        this.redisRepository = redisRepository;
    }

    @Override
    @Transactional
    public void makeAndSendData(long gameId, SendMessageService messageService) {
        InGame findInGame = getInGame(gameId);
        findInGame.clearDoubtData();
        redisRepository.saveInGame(gameId, findInGame);

        TurnData turn = getTurnData(findInGame);

        InGamePlayer defender = getInGamePlayer(gameId, turn.getDefenderId());

        DefenseResponse response = DefenseResponse.builder()
                .defender(MemberTeamResponse.builder()
                        .memberId(defender.getMemberId())
                        .nickname(defender.getNickname())
                        .team(defender.getTeam().name()).build())
                .weapon(Weapon.SHIELD.name())
                .hand(turn.getDefenseData().getDefendHand().name())
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
