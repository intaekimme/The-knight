package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.attack.response.AttackResponseDto;
import com.a301.theknight.domain.game.dto.attack.response.AttackTeamResponse;
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
public class GameAttackService {

    private final GameRedisRepository gameRedisRepository;

    public AttackResponseDto attack(long gameId, long memberId, GameAttackRequest gameAttackRequest){
        InGame findInGame = getInGame(gameId);
        TurnData turn = findInGame.getTurnData();

        turn.recordAttackTurn(gameAttackRequest);
        InGamePlayer attacker = getInGamePlayer(gameId, memberId);
        turn.checkLyingAttack(attacker);

        AttackTeamResponse allyResponse = AttackTeamResponse.builder()
                .attacker(new AttackPlayerDto(turn.getAttackerId()))
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(turn.getAttackData().getWeapon().name())
                .hand(turn.getAttackData().getAttackHand().name())
                .team(attacker.getTeam().name())
                .build();

        AttackTeamResponse oppResponse = AttackTeamResponse.builder()
                .attacker(new AttackPlayerDto(turn.getAttackerId()))
                .defender(new DefendPlayerDto(turn.getDefenderId()))
                .weapon(Weapon.HIDE.name())
                .hand(Hand.HIDE.name())
                .team(attacker.getTeam().name().equals("A") ? "B" : "A")
                .build();

        return AttackResponseDto.builder()
                .allyResponse(allyResponse)
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
