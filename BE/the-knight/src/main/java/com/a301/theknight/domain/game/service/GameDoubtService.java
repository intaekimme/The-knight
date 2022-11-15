package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.doubt.response.*;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.DoubtData;
import com.a301.theknight.domain.game.entity.redis.DoubtStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
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
    public void doubt(long gameId, long suspectId, long suspectedId, GameStatus doubtStatus) {
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
    public DoubtResponseDto getDoubtInfo(long gameId) {
        InGame inGame = getInGame(gameId);
        DoubtData doubtData = inGame.getTurnData().getDoubtData();

        DoubtResponse doubtResponse = makeDoubtResponse(gameId, doubtData);

        GameStatus nextStatus = getNextGameStatus(doubtData);
        inGame.changeStatus(nextStatus);
        inGame.clearDoubtData();
        gameRedisRepository.saveInGame(gameId, inGame);

        return new DoubtResponseDto(doubtResponse, inGame.getGameStatus());
    }

    @Transactional
    public DoubtPassResponse doubtPass(long gameId, long suspectId){
        RLock lock = redissonClient.getLock(lockKeyGen(gameId));
        try {
            boolean available = lock.tryLock(5, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new CustomRestException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
            }

            InGame inGame = getInGame(gameId);
            GameStatus gameStatus = inGame.getGameStatus();
            InGamePlayer suspect = getInGamePlayer(gameId, suspectId);
            if (suspect.isDead() || notDoubtStatus(gameStatus)) {
                return null;
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

            return DoubtPassResponse.builder()
                    .memberId(suspect.getMemberId())
                    .nickname(suspect.getNickname())
                    .build();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    // TODO
    public boolean notDoubtStatus(GameStatus gameStatus) {
        return !(ATTACK_DOUBT.equals(gameStatus) || DEFENSE_DOUBT.equals(gameStatus));
    }

    // TODO
    public GameStatus getNextGameStatus(DoubtData doubtData) {
        if (doubtData.isDeadLeader()) {
            return END;
        }
        if (doubtData.isDoubtResult()) {
            return ATTACK;
        }
        return DoubtStatus.ATTACK.equals(doubtData.getDoubtStatus()) ? DEFENSE : EXECUTE;
    }

    // TODO
    public DoubtResponse makeDoubtResponse(long gameId, DoubtData doubtData) {
        InGamePlayer suspect = getInGamePlayer(gameId, doubtData.getSuspectId());
        InGamePlayer suspected = getInGamePlayer(gameId, doubtData.getSuspectedId());

        return DoubtResponse.builder()
                .suspect(DoubtPlayerDto.toDto(suspect))
                .suspected(SuspectedPlayerDto.toDto(suspected, doubtData.getDoubtHand()))
                .doubtTeam(suspect.getTeam().name())
                .doubtResult(doubtData.isDoubtResult()).build();
    }

    // TODO
    public InGamePlayer killByDoubt(InGamePlayer suspect, InGamePlayer suspected, boolean isLying) {
        InGamePlayer deadPlayer = isLying ? suspected : suspect;
        deadPlayer.death();

        return deadPlayer;
    }

    // TODO
    public void validCheck(InGame inGame, GameStatus doubtStatus, InGamePlayer suspect, InGamePlayer suspected) {
        checkDoubtStatus(inGame, doubtStatus);
        checkDeadState(suspect, suspected);
        checkOtherTeam(suspect, suspected);
    }

    // TODO
    public void checkDoubtStatus(InGame inGame, GameStatus doubtStatus) {
        if (!doubtStatus.equals(inGame.getGameStatus())) {
            throw new CustomWebSocketException(DO_NOT_FIT_REQUEST_BY_GAME_STATUS);
        }
    }

    // TODO
    public void checkOtherTeam(InGamePlayer suspect, InGamePlayer suspected) {
        if (suspect.getTeam().equals(suspected.getTeam())) {
            throw new CustomWebSocketException(CAN_NOT_DOUBT_SAME_TEAM);
        }
    }

    // TODO
    public void checkDeadState(InGamePlayer suspect, InGamePlayer suspected) {
        if (suspect.isDead() || suspected.isDead()) {
            throw new CustomWebSocketException(PLAYER_IS_ALREADY_DEAD);
        }
    }
    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private InGamePlayer getInGamePlayer(long gameId, long suspectId) {
        return gameRedisRepository.getInGamePlayer(gameId, suspectId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    // TODO
    public String lockKeyGen(long gameId) {
        return "game:" + gameId + "_convert_lock";
    }

    // TODO
    public int getAlivePlayerCount(List<InGamePlayer> inGamePlayers){
        return (int)(inGamePlayers.stream().filter(inGamePlayer -> !inGamePlayer.isDead()).count());
    }
}
