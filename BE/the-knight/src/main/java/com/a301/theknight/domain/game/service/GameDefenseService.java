package com.a301.theknight.domain.game.service;


import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponseDto;
import com.a301.theknight.domain.game.dto.defense.response.DefenseTeamResponse;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.Hand;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@RequiredArgsConstructor
@Service
public class GameDefenseService {

    private final GameRedisRepository gameRedisRepository;

    public DefenseResponseDto defense(long gameId, long memberId, GameDefenseRequest gameDefenseRequest){
        InGame findInGame = getInGame(gameId);
        TurnData turn = findInGame.getTurnData();

        InGamePlayer defender = getInGamePlayer(gameId, memberId);
        turn.recordDefenseTurn(defender, gameDefenseRequest);
        turn.checkLyingDefense(defender);

        //  TODO 공격 로직 수정 후 InGame reopsitory에 저장, TurnData 왜 저장 할 것 있는지 생각후 작성,

        DefenseTeamResponse allayResponse = DefenseTeamResponse.builder()
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(Weapon.SHIELD.name())
                .hand(turn.getDefendData().getDefendHand().name())
                .team(defender.getTeam().name())
                .build();

        //  TODO 방어시 상대 팀에 방패와 손 정보를 전달할 것인지 논의
        DefenseTeamResponse oppResponse = DefenseTeamResponse.builder()
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(Weapon.HIDE.name())
                .hand(Hand.HIDE.name())
                .team(defender.getTeam().name().equals("A") ? "B" : "A")
                .build();

        return DefenseResponseDto.builder()
                .allyResponse(allayResponse)
                .oppResponse(oppResponse)
                .build();
    }


    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomException(INGAME_IS_NOT_EXIST));
    }


    private InGamePlayer getInGamePlayer(long gameId, long memberId) {
        return gameRedisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomException(INGAME_PLAYER_IS_NOT_EXIST));
    }
}
