package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtPlayerDto;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtResponse;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtResponseDto;
import com.a301.theknight.domain.game.dto.doubt.response.SuspectedPlayerDto;
import com.a301.theknight.domain.game.entity.redis.DoubtData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.util.GameLockUtil;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@Service
public class DoubtResultDataService extends GameDataService {

    private final GameRedisRepository redisRepository;

    public DoubtResultDataService(GameLockUtil gameLockUtil, GameRedisRepository redisRepository) {
        super(gameLockUtil, redisRepository);
        this.redisRepository = redisRepository;
    }

    @Override
    @Transactional
    public void makeAndSendData(long gameId, SendMessageService messageService) {
        InGame inGame = getInGame(gameId);
        DoubtData doubtData = inGame.getTurnData().getDoubtData();

        DoubtResponse doubtResponse = makeDoubtResponse(gameId, doubtData);

        DoubtResponseDto response = new DoubtResponseDto(doubtResponse, inGame.getGameStatus());
        messageService.sendData(gameId, "/doubt-info", response);
    }

    private DoubtResponse makeDoubtResponse(long gameId, DoubtData doubtData) {
        InGamePlayer suspect = getInGamePlayer(gameId, doubtData.getSuspectId());
        InGamePlayer suspected = getInGamePlayer(gameId, doubtData.getSuspectedId());

        return DoubtResponse.builder()
                .suspect(DoubtPlayerDto.toDto(suspect))
                .suspected(SuspectedPlayerDto.toDto(suspected, doubtData.getDoubtHand()))
                .doubtTeam(suspect.getTeam().name())
                .doubtSuccess(doubtData.isDoubtSuccess()).build();
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
