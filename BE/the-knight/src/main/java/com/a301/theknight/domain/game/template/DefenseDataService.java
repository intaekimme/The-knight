package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.player.response.MemberTeamResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.TeamInfoData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@Service
public class DefenseDataService extends GameDataService {

    private final GameRedisRepository redisRepository;

    public DefenseDataService(RedissonClient redissonClient, GameRedisRepository redisRepository) {
        super(redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void makeData(long gameId) {
    }

    @Override
    public void sendScreenData(long gameId, SendMessageService messageService) {
        InGame inGame = getInGame(gameId);
        TeamInfoData teamInfoData = getTeamInfoData(inGame);

        GameOrderDto attacker = teamInfoData.getOrderList()[teamInfoData.getCurrentAttackIndex()];
        MemberTeamResponse response = MemberTeamResponse.builder()
                .memberId(attacker.getMemberId())
                .nickname(attacker.getNickname())
                .team(inGame.getCurrentAttackTeam().name()).build();

        messageService.sendData(gameId, "/attacker", response);
    }

    private TeamInfoData getTeamInfoData(InGame inGame) {
        return Team.A.equals(inGame.getCurrentAttackTeam()) ?
                inGame.getTeamAInfo() : inGame.getTeamBInfo();
    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }
}
