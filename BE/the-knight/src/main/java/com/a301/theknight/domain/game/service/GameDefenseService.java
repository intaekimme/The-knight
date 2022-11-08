package com.a301.theknight.domain.game.service;


import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import com.a301.theknight.domain.game.dto.defense.request.GameDefensePassRequest;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.DefendData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GameDefenseService {

    private final GameRedisRepository gameRedisRepository;

    @Transactional
    public void defense(long gameId, long memberId, GameDefenseRequest gameDefenseRequest){
        checkPlayerId(memberId, gameDefenseRequest.getDefender().getId());
        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        InGamePlayer defender = getInGamePlayer(gameId,  gameDefenseRequest.getDefender().getId());
        turn.recordDefenseTurn(defender, gameDefenseRequest);
        turn.checkLyingDefense(defender);

        findInGame.recordTurnData(turn);
        findInGame.changeStatus(GameStatus.DEFENSE_DOUBT);
        gameRedisRepository.saveInGame(gameId, findInGame);

    }

    @Transactional
    public DefenseResponse getDefenseInfo(long game) {
        InGame findInGame = getInGame(game);
        TurnData turn = getTurnData(findInGame);

        return DefenseResponse.builder()
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(Weapon.SHIELD.name())
                .hand(turn.getDefendData().getDefendHand().name())
                .build();
    }

    @Transactional
    public void isDefensePass(long gameId, GameDefensePassRequest gameDefensePassRequest, long memberId){
        checkPlayerId(memberId, gameDefensePassRequest.getDefender().getId());

        InGame findInGame = getInGame(gameId);
        if(findInGame.getGameStatus().equals(GameStatus.DEFENSE)){
            TurnData turnData = findInGame.getTurnData();
            DefendData defendData = turnData.getDefendData();
            //  방어자 방어 패스처리
            defendData.defendPass();
            //  방어를 패스하므로 공격 모션로 전환
            findInGame.changeStatus(GameStatus.EXECUTE);
            //  수정한 인게임 저장
            gameRedisRepository.saveInGame(gameId, findInGame);
        }
        throw new CustomWebSocketException(UNABLE_TO_PASS_DEFENSE);
    }


    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }


    private InGamePlayer getInGamePlayer(long gameId, long memberId) {
        return gameRedisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private TurnData getTurnData(InGame inGame){
        if(inGame.isTurnDataEmpty()){
            inGame.initTurnData();
        }
        return inGame.getTurnData();
    }

    private void checkPlayerId(long memberId, long playerId){
        if (memberId != playerId) {
            throw new CustomWebSocketException(PLAYER_IS_NOT_USER_WHO_LOGGED_IN);
        }
    }
}
