package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.attacker.AttackerDto;
import com.a301.theknight.domain.game.dto.attacker.response.AttackerResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TeamInfoData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class GameAttackerService {

    private final GameRedisRepository gameRedisRepository;

    @Transactional
    public AttackerDto getAttacker(long gameId) {

        AttackerDto attackerDto = null;
        InGame inGame = gameRedisRepository.getInGame(gameId).orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
        inGame.updateCurrentAttackTeam();
        TeamInfoData teamInfoData = inGame.getCurrentAttackTeam() == Team.A ? inGame.getTeamAInfo() : inGame.getTeamBInfo();
        int capacity = inGame.getMaxMemberNum() / 2;
        int attackerIndex = teamInfoData.getCurrentAttackIndex();
        GameOrderDto[] orderList = teamInfoData.getOrderList();

        while (attackerDto == null) {
            attackerIndex = ++attackerIndex % capacity;
            long memberId = orderList[attackerIndex].getMemberId();
            InGamePlayer player = gameRedisRepository.getInGamePlayer(gameId, memberId).orElseThrow(() -> new CustomRestException(INGAME_PLAYER_IS_NOT_EXIST));
            if (!player.isDead()) {
                teamInfoData.updateCurrentAttackIndex(attackerIndex);
                attackerDto = AttackerDto.builder()
                        .attackerResponseA(AttackerResponse.builder().memberId(memberId).isOpposite(player.getTeam() != Team.A).build())
                        .attackerResponseB(AttackerResponse.builder().memberId(memberId).isOpposite(player.getTeam() != Team.B).build())
                        .build();
            }
        }

        gameRedisRepository.saveInGame(gameId, inGame);

        return attackerDto;
    }
}
