package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.doubt.response.DoubtPlayerDto;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtResponse;
import com.a301.theknight.domain.game.dto.doubt.response.SuspectedPlayerDto;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.a301.theknight.domain.game.entity.GameStatus.*;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GameDoubtService {

    private final GameRedisRepository gameRedisRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public void doubt(long gameId, long suspectId, long suspectedId, String doubtStatus) {
        InGame inGame = getInGame(gameId);
        InGamePlayer suspect = getInGamePlayer(gameId, suspectId);
        InGamePlayer suspected = getInGamePlayer(gameId, suspectedId);

        validCheck(inGame, doubtStatus, suspect, suspected);

        boolean isLying = inGame.getLyingData();
        InGamePlayer deadPlayer = killByDoubt(suspect, suspected, isLying);
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

    @Transactional
    public void doubtPass(long gameId, long suspectId){
        RLock lock = redissonClient.getLock(lockKeyGen(gameId));
        try {
            boolean available = lock.tryLock(5, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new CustomException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
            }

            InGame inGame = getInGame(gameId);
            GameStatus gameStatus = inGame.getGameStatus();
            InGamePlayer suspect = getInGamePlayer(gameId, suspectId);
            if (suspect.isDead() || notDoubtStatus(gameStatus)) {
                return;
            }

            Team suspectTeam = suspect.getTeam();
            int alivePlayerCount = Team.A.equals(suspectTeam) ?
                    getAlivePlayerCount(gameRedisRepository.getTeamPlayerList(gameId, Team.A)) :
                    getAlivePlayerCount(gameRedisRepository.getTeamPlayerList(gameId, Team.B));

            inGame.addDoubtPassCount();
            if(inGame.getDoubtPassCount() >= alivePlayerCount){
                GameStatus nextStatus = ATTACK_DOUBT.equals(inGame.getGameStatus()) ? DEFENSE : EXECUTE;
                inGame.changeStatus(nextStatus);

                inGame.initDoubtPassCount();
            }
            gameRedisRepository.saveInGame(gameId, inGame);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private boolean notDoubtStatus(GameStatus gameStatus) {
        return !(ATTACK_DOUBT.equals(gameStatus) || DEFENSE_DOUBT.equals(gameStatus));
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

    private String lockKeyGen(long gameId) {
        return "game:" + gameId + "_convert_lock";
    }

    private int getAlivePlayerCount(List<InGamePlayer> inGamePlayers){
        return (int)(inGamePlayers.stream().filter(inGamePlayer -> !inGamePlayer.isDead()).count());
    }
}
