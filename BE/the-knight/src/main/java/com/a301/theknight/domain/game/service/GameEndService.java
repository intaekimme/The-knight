package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.end.GameEndDto;
import com.a301.theknight.domain.game.dto.end.PlayerWeaponDto;
import com.a301.theknight.domain.game.dto.end.response.EndResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TeamInfoData;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.a301.theknight.global.error.errorcode.GameErrorCode.GAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.PlayerErrorCode.PLAYER_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.RankingErrorCode.RANKING_IS_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class GameEndService {

    private final GameRedisRepository gameRedisRepository;
    private final GameRepository gameRepository;
    private final RankingRepository rankingRepository;
    private final PlayerRepository playerRepository;

    @Transactional
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

}
