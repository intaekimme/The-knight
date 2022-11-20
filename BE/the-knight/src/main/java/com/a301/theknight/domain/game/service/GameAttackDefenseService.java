package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.attack.response.AttackResponse;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponse;
import com.a301.theknight.domain.game.dto.player.response.MemberTeamResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.dto.prepare.response.GamePreAttackResponse;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.a301.theknight.domain.game.entity.GameStatus.ATTACK;
import static com.a301.theknight.domain.game.entity.GameStatus.DEFENSE;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@RequiredArgsConstructor
@Service
public class GameAttackDefenseService {

    private final GameRedisRepository gameRedisRepository;

    //  Attacker
    @Transactional
    public MemberTeamResponse getAttacker(long gameId) {

        MemberTeamResponse memberTeamResponse = null;
        InGame inGame = getInGame(gameId);
        TeamInfoData teamInfoData = inGame.updateCurrentAttackTeam() == Team.A ? inGame.getTeamAInfo() : inGame.getTeamBInfo();
        int capacity = inGame.getMaxMemberNum() / 2;
        int attackerIndex = teamInfoData.getCurrentAttackIndex();
        GameOrderDto[] orderList = teamInfoData.getOrderList();

        while (memberTeamResponse == null) {
            attackerIndex = ++attackerIndex % capacity;
            long memberId = orderList[attackerIndex].getMemberId();
            InGamePlayer player = getInGamePlayer(gameId, memberId);
            if (!player.isDead()) {
                teamInfoData.updateCurrentAttackIndex(attackerIndex);
                memberTeamResponse = MemberTeamResponse.builder()
                        .memberId(memberId)
                        .team(player.getTeam().name()).build();
            }
        }

        gameRedisRepository.saveInGame(gameId, inGame);

        return memberTeamResponse;
    }

    //  Attack
    @Transactional
    public GamePreAttackResponse getPreAttack(long gameId) {
        InGame inGame = getInGame(gameId);
        Team preAttackTeam = inGame.getCurrentAttackTeam();

        gameRedisRepository.saveInGame(gameId, inGame);

        return new GamePreAttackResponse(preAttackTeam);
    }


    @Transactional
    public void attack(long gameId, long memberId, GameAttackRequest gameAttackRequest) {
        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        InGamePlayer attacker = getInGamePlayer(gameId, memberId);
        InGamePlayer defender = getInGamePlayer(gameId, gameAttackRequest.getDefender().getMemberId());
        turn.recordAttackData(attacker, defender, gameAttackRequest);
        turn.recordAttackLying(attacker);

        gameRedisRepository.saveInGame(gameId, findInGame);
    }

    @Transactional
    public AttackResponse getAttackInfo(long gameId) {

        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        long attackerId = turn.getAttackerId();
        long defenderId = turn.getDefenderId();

        return AttackResponse.builder()
                .attacker(MemberTeamResponse.builder()
                        .memberId(attackerId)
                        .team(getInGamePlayer(gameId, attackerId).getTeam().name())
                        .build())
                .defender(MemberTeamResponse.builder()
                        .memberId(defenderId)
                        .team(getInGamePlayer(gameId, defenderId).getTeam().name())
                        .build())
                .weapon(turn.getAttackData().getWeapon().name())
                .hand(turn.getAttackData().getAttackHand().name())
                .build();
    }

    @Transactional
    public void checkAttackPass(long gameId, long memberId) {
        InGame findInGame = getInGame(gameId);
        getInGamePlayer(gameId, memberId);

        if (findInGame.getGameStatus().equals(ATTACK)) {
            findInGame.addTurn();
            gameRedisRepository.saveInGame(gameId, findInGame);
            return;
        }
        throw new CustomWebSocketException(UNABLE_TO_PASS_ATTACK);
    }

    //  Defense
    @Transactional
    public void defense(long gameId, long memberId, GameDefenseRequest gameDefenseRequest) {
        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        InGamePlayer defender = getInGamePlayer(gameId, memberId);
        turn.recordDefenseData(defender, gameDefenseRequest);
        turn.recordDefenseLying(defender);

        gameRedisRepository.saveInGame(gameId, findInGame);
    }

    @Transactional
    public DefenseResponse getDefenseInfo(long gameId) {
        InGame findInGame = getInGame(gameId);
        TurnData turn = getTurnData(findInGame);

        long defenderId = turn.getDefenderId();

        return DefenseResponse.builder()
                .defender(MemberTeamResponse.builder()
                        .memberId(defenderId)
                        .team(getInGamePlayer(gameId, defenderId).getTeam().name())
                        .build())
                .weapon(Weapon.SHIELD.name())
                .hand(turn.getDefenseData().getDefendHand().name())
                .build();
    }

    @Transactional
    public void isDefensePass(long gameId, long memberId) {
        InGame findInGame = getInGame(gameId);
//        getInGamePlayer(gameId, memberId);
        if (!DEFENSE.equals(findInGame.getGameStatus())) {
            throw new CustomWebSocketException(UNABLE_TO_PASS_DEFENSE);
        }
        TurnData turnData = findInGame.getTurnData();
        DefendData defendData = turnData.getDefenseData();
        defendData.defendPass();

        gameRedisRepository.saveInGame(gameId, findInGame);
    }

    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private InGamePlayer getInGamePlayer(long gameId, long memberId) {
        return gameRedisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private TurnData getTurnData(InGame inGame) {
        if (inGame.isTurnDataEmpty()) {
            inGame.initTurnData();
        }
        return inGame.getTurnData();
    }

}
