package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.doubt.response.DoubtPlayerDto;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtResponse;
import com.a301.theknight.domain.game.dto.doubt.response.SuspectedPlayerDto;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.*;
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

    @Transactional
    public void doubtPass(long gameId, long suspectId, String doubtStatus){
        RLock lock = redissonClient.getLock(lockKeyGen(gameId));
        try {
            boolean available = lock.tryLock(5, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new CustomRestException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
            }

            InGame inGame = getInGame(gameId);
            InGamePlayer suspect = getInGamePlayer(gameId, suspectId);

            // 의심 패스 요청 보낸 사람의 팀 정보와 죽어있는지 확인
                //  죽었으면 의심 패스 종료
            if(suspect.isDead()) return;
                //  살아 있으면
                    // 인게임의 현재 의심 패스 카운트 갯수 확인
            int curDoubtPassCount = inGame.getDoubtPassCount();
            //  이전에 확인한 팀 정보로 인게임에서 팀 데이터 얻기
            Team suspectTeam = suspect.getTeam();

                    //  팀데이터의 orderlist 순회하면서 memberId가지고 인게임 내 해당 플레이어가 생존해 있는지 확인
                    //  팀 내 총 생존한 플레이어 갯수가 나옴
            int alivePlayerCount = suspectTeam.equals(Team.A) ?
                    getAlivePlayerCount(gameRedisRepository.getTeamPlayerList(gameId, Team.A)) :
                    getAlivePlayerCount(gameRedisRepository.getTeamPlayerList(gameId, Team.B));

                    //  앞서 확인한 인게임 의심 패스 카운트가 < 생존한 사람 수 - 1
            if(curDoubtPassCount < alivePlayerCount - 1){
                        //  인게임 의심패스 카운트 증가
                inGame.addDoubtPassCount();
                gameRedisRepository.saveInGame(gameId, inGame);
                        //  화면 전환 없음
                        //  return
            }
                    //  의심 패스 카운트 == 생존한 사람 수 - 1
            if(curDoubtPassCount == alivePlayerCount - 1){
                        //  의심 갯수 초기화
                inGame.initDoubtPassCount();
                        //  의심 결과로 화면 전환
                if(inGame.getGameStatus().equals(ATTACK_DOUBT)) inGame.changeStatus(DEFENSE);
                else if(inGame.getGameStatus().equals(DEFENSE_DOUBT)) inGame.changeStatus(EXECUTE);
                gameRedisRepository.saveInGame(gameId, inGame);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
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
            throw new CustomWebSocketException(DO_NOT_FIT_REQUEST_BY_GAME_STATUS);
        }
    }

    private void checkOtherTeam(InGamePlayer suspect, InGamePlayer suspected) {
        if (suspect.getTeam().equals(suspected.getTeam())) {
            throw new CustomWebSocketException(CAN_NOT_DOUBT_SAME_TEAM);
        }
    }

    private void checkDeadState(InGamePlayer suspect, InGamePlayer suspected) {
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

    private String lockKeyGen(long gameId) {
        return "game:" + gameId + "_convert_lock";
    }

    private int getAlivePlayerCount(List<InGamePlayer> inGamePlayers){
        return (int)(inGamePlayers.stream().filter(inGamePlayer -> !inGamePlayer.isDead()).count());
    }
}
