package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.end.GameEndDto;
import com.a301.theknight.domain.game.dto.end.PlayerWeaponDto;
import com.a301.theknight.domain.game.dto.end.response.EndResponse;
import com.a301.theknight.domain.game.dto.execute.response.AttackerDto;
import com.a301.theknight.domain.game.dto.execute.response.DefenderDto;
import com.a301.theknight.domain.game.dto.execute.response.GameExecuteResponse;
import com.a301.theknight.domain.game.dto.pass.response.PassResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.domain.ranking.entity.Ranking;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.a301.theknight.global.error.errorcode.GameErrorCode.GAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.PlayerErrorCode.PLAYER_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.RankingErrorCode.RANKING_IS_NOT_EXIST;

@RequiredArgsConstructor
@Service
public class GameExecuteEndService {

    private final GameRedisRepository gameRedisRepository;
    private final GameRepository gameRepository;
    private final RankingRepository rankingRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public GameExecuteResponse executeTurn(long gameId) {
        InGame inGame = getInGame(gameId);
        TurnData turnData = inGame.getTurnData();

        InGamePlayer defender = getInGamePlayer(gameId, turnData.getDefenderId());
        AttackData attackData = turnData.getAttackData();
        DefendData defendData = turnData.getDefendData();

        int defendCount = defendData.getShieldCount();
        boolean isDefendPass = defendData.isDefendPass();

        Weapon attackWeapon = attackData.getWeapon();
        int resultCount = defendCount - attackWeapon.getCount();

        GameStatus nextStatus = GameStatus.ATTACK;
        if (resultCount < 0 || isDefendPass) {
            defender.death();
            if (defender.isLeader()) {
                nextStatus = GameStatus.END;
            }
        } else {
            defender.changeCount(resultCount, defendData.getDefendHand());
        }

        inGame.changeStatus(nextStatus);
        gameRedisRepository.saveInGame(gameId, inGame);
        gameRedisRepository.saveInGamePlayer(gameId, defender.getMemberId(), defender);

        return getGameExecuteResponse(inGame, turnData, defender, resultCount);
    }

    //  End
    @javax.transaction.Transactional
    public GameEndDto gameEnd(long gameId) {

        // GameEnd 비즈니스 로직 수행
        // 1. player update(result만)
        // 2. Ranking 점수 갱신
        // 3. 게임 상태 End로 update
        // 4. GameEndDto 채워서 리턴

        InGame inGame = gameRedisRepository.getInGame(gameId).orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new CustomRestException(GAME_IS_NOT_EXIST));
        game.changeStatus(GameStatus.END);

        List<PlayerWeaponDto> players = new ArrayList<>();

        boolean isAWin = method(gameId, players, inGame.getTeamAInfo());
        boolean isBWin = method(gameId, players, inGame.getTeamBInfo());

        String losingTeam = isAWin ? "A" : "B";
        long losingLeaderId = losingTeam.equals("A") ? inGame.getTeamAInfo().getLeaderId() : inGame.getTeamBInfo().getLeaderId();
        long winningLeaderId = losingTeam.equals("A") ? inGame.getTeamBInfo().getLeaderId() : inGame.getTeamAInfo().getLeaderId();

        EndResponse endResponseA = EndResponse.builder().isWin(isAWin).losingTeam(losingTeam).losingLeaderId(losingLeaderId).winningLeaderId(winningLeaderId).build();
        EndResponse endResponseB = EndResponse.builder().isWin(isBWin).losingTeam(losingTeam).losingLeaderId(losingLeaderId).winningLeaderId(winningLeaderId).build();

        return GameEndDto.builder().endResponseA(endResponseA).endResponseB(endResponseB).build();
    }

    private boolean method(long gameId, List<PlayerWeaponDto> players, TeamInfoData teamInfoData) {
        long LeaderId = teamInfoData.getLeaderId();
        InGamePlayer Leader = gameRedisRepository.getInGamePlayer(gameId, LeaderId).orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
        boolean isWin = !Leader.isDead();

        GameOrderDto[] orderList = teamInfoData.getOrderList();

        for (GameOrderDto gameOrderDto : orderList) {
            long memberId = gameOrderDto.getMemberId();
            Player player = playerRepository.findByGameIdAndMemberId(gameId, memberId).orElseThrow(() -> new CustomRestException(PLAYER_IS_NOT_EXIST));
            Ranking ranking = rankingRepository.findByMemberId(memberId).orElseThrow(() -> new CustomRestException(RANKING_IS_NOT_EXIST));

            if (isWin) {
                player.winGame();
                ranking.saveWinScore();
            } else {
                player.loseGame();
                ranking.saveLoseScore();
            }

            InGamePlayer inGamePlayer = gameRedisRepository.getInGamePlayer(gameId, memberId).orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
            players.add(PlayerWeaponDto.builder().memberId(memberId).leftWeapon(inGamePlayer.getLeftWeapon().toString()).rightWeapon(inGamePlayer.getRightWeapon().toString()).build());
        }

        return isWin;
    }

    private GameExecuteResponse getGameExecuteResponse(InGame inGame, TurnData turnData, InGamePlayer defender, int nextCount) {
        AttackData attackData = turnData.getAttackData();
        DefendData defendData = turnData.getDefendData();

        AttackerDto attackerDto = AttackerDto.builder()
                .id(turnData.getAttackerId())
                .hand(attackData.getAttackHand().name())
                .weapon(attackData.getWeapon().name())
                .build();
        DefenderDto defenderDto = DefenderDto.builder()
                .id(turnData.getDefenderId())
                .hand(defendData.getDefendHand().name())
                .isDead(defender.isDead())
                .restCount(nextCount)
                .build();

        return GameExecuteResponse.builder()
                .attackTeam(inGame.getCurrentAttackTeam().name())
                .attacker(attackerDto)
                .defender(defenderDto)
                .build();
    }

    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private InGamePlayer getInGamePlayer(long gameId, Long memberId) {
        return gameRedisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }
}
