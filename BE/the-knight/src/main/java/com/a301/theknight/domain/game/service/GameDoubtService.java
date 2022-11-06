package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.doubt.response.DoubtPlayerDto;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtResponse;
import com.a301.theknight.domain.game.dto.doubt.response.SuspectedPlayerDto;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.DoubtData;
import com.a301.theknight.domain.game.entity.redis.DoubtStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.a301.theknight.domain.game.entity.GameStatus.*;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GameDoubtService {

    private final GameRedisRepository gameRedisRepository;

    @Transactional
    public void doubt(long gameId, long suspectId, long suspectedId, String doubtStatus) {
        InGame inGame = getInGame(gameId);
        InGamePlayer suspect = getInGamePlayer(gameId, suspectId);
        InGamePlayer suspected = getInGamePlayer(gameId, suspectedId);

        validCheck(inGame, doubtStatus, suspect, suspected);

        boolean isLying = inGame.getLyingData();
        InGamePlayer deadPlayer = killByDoubt(suspect, suspected, isLying);
        inGame.changeStatus(DOUBT_RESULT);
        gameRedisRepository.saveInGamePlayer(gameId, deadPlayer.getMemberId(), deadPlayer);

        inGame.setDoubtData(DoubtData.builder()
                        .suspectId(suspectId)
                        .suspectedId(suspectedId)
                        .doubtResult(deadPlayer.equals(suspected))
                        .doubtStatus(ATTACK_DOUBT.equals(inGame.getGameStatus()) ? DoubtStatus.ATTACK : DoubtStatus.DEFENSE)
                        .deadLeader(deadPlayer.isLeader()).build());
        inGame.changeStatus(DOUBT_RESULT);
        gameRedisRepository.saveInGame(gameId, inGame);
    }

    @Transactional
    public DoubtResponse getDoubtInfo(long gameId) {
        InGame inGame = getInGame(gameId);
        DoubtData doubtData = inGame.getTurnData().getDoubtData();

        DoubtResponse doubtResponse = makeDoubtResponse(gameId, doubtData);

        GameStatus nextStatus = getNextGameStatus(doubtData);
        inGame.changeStatus(nextStatus);
        inGame.clearDoubtData();
        gameRedisRepository.saveInGame(gameId, inGame);

        return doubtResponse;
    }

    private GameStatus getNextGameStatus(DoubtData doubtData) {
        if (doubtData.isDeadLeader()) {
            return END;
        }
        if (doubtData.isDoubtResult()) {
            return ATTACK;
        }
        return DoubtStatus.ATTACK.equals(doubtData.getDoubtStatus()) ? DEFENSE : EXECUTE;
    }

    private DoubtResponse makeDoubtResponse(long gameId, DoubtData doubtData) {
        InGamePlayer suspect = getInGamePlayer(gameId, doubtData.getSuspectId());
        InGamePlayer suspected = getInGamePlayer(gameId, doubtData.getSuspectedId());

        return DoubtResponse.builder()
                .suspect(DoubtPlayerDto.toDto(suspect))
                .suspected(SuspectedPlayerDto.toDto(suspected, doubtData.getDoubtHand()))
                .doubtTeam(suspect.getTeam().name())
                .doubtResult(doubtData.isDoubtResult()).build();
    }

    private InGamePlayer killByDoubt(InGamePlayer suspect, InGamePlayer suspected, boolean isLying) {
        InGamePlayer deadPlayer = isLying ? suspected : suspect;
        deadPlayer.death();

        return deadPlayer;
    }

    private void validCheck(InGame inGame, String doubtStatus, InGamePlayer suspect, InGamePlayer suspected) {
        checkDoubtStatus(inGame, doubtStatus);
        checkDeadState(suspect, suspected);
        checkOtherTeam(suspect, suspected);
    }

    private void checkDoubtStatus(InGame inGame, String doubtStatus) {
        String gameStatus = inGame.getGameStatus().name();
        if (!gameStatus.equals(doubtStatus)) {
            throw new CustomException(DO_NOT_FIT_REQUEST_BY_GAME_STATUS);
        }
    }

    private void checkOtherTeam(InGamePlayer suspect, InGamePlayer suspected) {
        if (suspect.getTeam().equals(suspected.getTeam())) {
            throw new CustomException(CAN_NOT_DOUBT_SAME_TEAM);
        }
    }

    private void checkDeadState(InGamePlayer suspect, InGamePlayer suspected) {
        if (suspect.isDead() || suspected.isDead()) {
            throw new CustomException(PLAYER_IS_ALREADY_DEAD);
        }
    }

    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomException(INGAME_IS_NOT_EXIST));
    }

    private InGamePlayer getInGamePlayer(long gameId, long suspectId) {
        return gameRedisRepository.getInGamePlayer(gameId, suspectId)
                .orElseThrow(() -> new CustomException(INGAME_PLAYER_IS_NOT_EXIST));
    }

}
