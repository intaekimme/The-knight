package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.doubt.request.DoubtPlayerIdDto;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtPlayerDto;
import com.a301.theknight.domain.game.dto.doubt.response.DoubtResponse;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GameDoubtService {

    private final GameRedisRepository gameRedisRepository;

    @Transactional
    public DoubtResponse doubt(long gameId, long suspectId, long suspectedId) {
        InGame inGame = getInGame(gameId);
        InGamePlayer suspect = getInGamePlayer(gameId, suspectId);
        InGamePlayer suspected = getInGamePlayer(gameId, suspectedId);

        validCheck(inGame, suspect, suspected);

        //의심 로직 수행
        TurnData turnData = inGame.getTurnData();
        //1. 공격의심인지 방어의심인지에 따라 거짓말 여부 flag 리턴
        boolean isAttackDoubt = isAttackDoubt(turnData);
        //1. 이전 행동에 사용자가 거짓말을 했는지 flag체크
        boolean isLying = getLyingData(isAttackDoubt, turnData);
        //2. 거짓말을 했으면 의심받는사람, 안했으면 의심 하는 사람이 죽기
       doDoubt(suspect, suspected, isLying);
        //응답 만들어서 리턴
        return DoubtResponse.builder()
                .suspect(DoubtPlayerDto.toDto(suspect))
                .suspected(DoubtPlayerDto.toDto(suspected))
                .keepDefense(isAttackDoubt && !isLying)
                .nextDefender(new DoubtPlayerIdDto(turnData.getDefenderId()))
                .SuspiciousResult(isLying)
                .build();
    }

    private void doDoubt(InGamePlayer suspect, InGamePlayer suspected, boolean isLying) {
        InGamePlayer deadPlayer = isLying ? suspected : suspect;
        deadPlayer.death();
    }

    private boolean getLyingData(boolean isAttackDoubt, TurnData turnData) {
        return isAttackDoubt ? turnData.isLyingAttack() : turnData.isLyingDefend();
    }

    private boolean isAttackDoubt(TurnData turnData) {
        return turnData.getDefendData() == null;
    }

    private void validCheck(InGame inGame, InGamePlayer suspect, InGamePlayer suspected) {
        //유효성 검사
        //1. inGame의 상태
        //2. 두 사람 중 죽어 있는 사람이 있는지
        checkDeadState(suspect, suspected);
        //3. 두 사람이 다른 팀인지
        checkOtherTeam(suspect, suspected);
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
