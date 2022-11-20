package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.doubt.response.*;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.DoubtData;
import com.a301.theknight.domain.game.entity.redis.DoubtStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.util.GameConvertUtil;
import com.a301.theknight.domain.game.util.GameLockUtil;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import static com.a301.theknight.domain.game.entity.GameStatus.*;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameDoubtService {
    private final GameRedisRepository gameRedisRepository;
    private final GameLockUtil gameLockUtil;
    private final Map<Long, ConcurrentHashMap<Long, String>> passMap = new ConcurrentHashMap<>();

    @Transactional
    public void doubt(long gameId, long suspectId, long suspectedId, GameStatus doubtStatus) {
        InGame inGame = getInGame(gameId);
        InGamePlayer suspect = getInGamePlayer(gameId, suspectId);
        InGamePlayer suspected = getInGamePlayer(gameId, suspectedId);

        validCheck(inGame, doubtStatus, suspect, suspected);

        boolean isLying = inGame.getLyingData();
        InGamePlayer deadPlayer = killByDoubt(suspect, suspected, isLying);

        saveDoubtData(suspectId, suspectedId, inGame, suspected, deadPlayer);
        inGame.addTurn();

        gameRedisRepository.saveInGamePlayer(gameId, deadPlayer.getMemberId(), deadPlayer);
        gameRedisRepository.saveInGame(gameId, inGame);
    }

    @Transactional
    public DoubtResponseDto getDoubtInfo(long gameId) {
        InGame inGame = getInGame(gameId);
        DoubtData doubtData = inGame.getTurnData().getDoubtData();

        DoubtResponse doubtResponse = makeDoubtResponse(gameId, doubtData);

        inGame.clearDoubtData();
        gameRedisRepository.saveInGame(gameId, inGame);

        return new DoubtResponseDto(doubtResponse, inGame.getGameStatus());
    }

    @Transactional
    public DoubtPassDto doubtPass(long gameId, long suspectId){
        gameLockUtil.doubtPassLock(gameId,7, 2);

        InGame inGame = getInGame(gameId);
        GameStatus gameStatus = inGame.getGameStatus();
        InGamePlayer suspect = getInGamePlayer(gameId, suspectId);
        if (suspect.isDead() || notDoubtStatus(gameStatus)) {
            throw new CustomWebSocketException(UNABLE_TO_PASS_DOUBT);
        }

        inGame.addDoubtPassCount();
        gameRedisRepository.saveInGame(gameId, inGame);

        Team suspectTeam = suspect.getTeam();
        int alivePlayerCount = getAlivePlayerCount(gameId, suspectTeam);

        gameLockUtil.doubtPassUnLock(gameId);
        return DoubtPassDto.builder()
                .fullCount(inGame.getDoubtPassCount() >= alivePlayerCount)
                .doubtPassResponse(DoubtPassResponse.builder()
                        .memberId(suspect.getMemberId())
                        .nickname(suspect.getNickname()).build())
                .build();
    }

    private void saveDoubtData(long suspectId, long suspectedId, InGame inGame, InGamePlayer suspected, InGamePlayer deadPlayer) {
        DoubtData doubtData = inGame.getTurnData().getDoubtData();

        doubtData.setSuspectId(suspectId);
        doubtData.setSuspectedId(suspectedId);
        doubtData.setDoubtSuccess(deadPlayer.getMemberId().equals(suspected.getMemberId()));
        doubtData.setDoubtHand(ATTACK_DOUBT.equals(inGame.getGameStatus()) ?
                inGame.getTurnData().getAttackData().getAttackHand() :
                inGame.getTurnData().getDefenseData().getDefendHand());
        doubtData.setDoubtStatus(ATTACK_DOUBT.equals(inGame.getGameStatus()) ? DoubtStatus.ATTACK : DoubtStatus.DEFENSE);
        doubtData.setDeadLeader(deadPlayer.isLeader());
    }

    // TODO
    public boolean notDoubtStatus(GameStatus gameStatus) {
        return !(ATTACK_DOUBT.equals(gameStatus) || DEFENSE_DOUBT.equals(gameStatus));
    }


    // TODO
    public DoubtResponse makeDoubtResponse(long gameId, DoubtData doubtData) {
        InGamePlayer suspect = getInGamePlayer(gameId, doubtData.getSuspectId());
        InGamePlayer suspected = getInGamePlayer(gameId, doubtData.getSuspectedId());

        return DoubtResponse.builder()
                .suspect(DoubtPlayerDto.toDto(suspect))
                .suspected(SuspectedPlayerDto.toDto(suspected, doubtData.getDoubtHand()))
                .doubtTeam(suspect.getTeam().name())
                .doubtSuccess(doubtData.isDoubtSuccess()).build();
    }

    // TODO
    public InGamePlayer killByDoubt(InGamePlayer suspect, InGamePlayer suspected, boolean isLying) {
        InGamePlayer deadPlayer = isLying ? suspected : suspect;
        deadPlayer.death();

        return deadPlayer;
    }

    private int getAlivePlayerCount(long gameId, Team suspectTeam) {
        List<InGamePlayer> teamPlayerList = gameRedisRepository.getTeamPlayerList(gameId, suspectTeam);

        return (int) teamPlayerList.stream()
                .filter(inGamePlayer -> !inGamePlayer.isDead())
                .count();
    }

    // TODO
    public void validCheck(InGame inGame, GameStatus doubtStatus, InGamePlayer suspect, InGamePlayer suspected) {
        checkDoubtStatus(inGame, doubtStatus);
        checkDeadState(suspect, suspected);
        checkOtherTeam(suspect, suspected);
    }

    // TODO
    public void checkDoubtStatus(InGame inGame, GameStatus doubtStatus) {
//        if (!doubtStatus.equals(inGame.getGameStatus())) {
//            log.info("Not Equal Status : Reuqest = {}, inGame = {}", doubtStatus.name(), inGame.getGameStatus().name());
//            throw new CustomWebSocketException(DO_NOT_FIT_REQUEST_BY_GAME_STATUS);
//        }
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

}
