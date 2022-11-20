package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.end.response.GameEndResponse;
import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.game.util.GameLockUtil;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.domain.ranking.entity.Ranking;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.a301.theknight.global.error.errorcode.GameErrorCode.GAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.PlayerErrorCode.PLAYER_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.RankingErrorCode.RANKING_IS_NOT_EXIST;

@Service
public class EndDataService extends GameDataService {

    private final GameRedisRepository redisRepository;
    private final GameRepository gameRepository;
    private final RankingRepository rankingRepository;
    private final PlayerRepository playerRepository;

    public EndDataService(GameLockUtil gameLockUtil, GameRedisRepository redisRepository, GameRepository gameRepository,
                          RankingRepository rankingRepository, PlayerRepository playerRepository) {
        super(gameLockUtil, redisRepository);
        this.redisRepository = redisRepository;
        this.gameRepository = gameRepository;
        this.rankingRepository = rankingRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public void makeAndSendData(long gameId, SendMessageService messageService) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomWebSocketException(GAME_IS_NOT_EXIST));
        game.changeStatus(GameStatus.END);

        InGame inGame = getInGame(gameId);

        long teamALeaderId = inGame.getTeamAInfo().getLeaderId();
        long teamBLeaderId = inGame.getTeamBInfo().getLeaderId();

        String winningTeam = getInGamePlayer(gameId, teamALeaderId).isDead() ? "B" : "A";
        List<PlayerDataDto> players = updatePlayerAndRanking(gameId, winningTeam);

        GameEndResponse response = GameEndResponse.builder()
                .winningTeam(winningTeam)
                .teamALeaderId(teamALeaderId)
                .teamBLeaderId(teamBLeaderId)
                .players(players).build();
        messageService.sendData(gameId, "/end", response);
    }

    private List<PlayerDataDto> updatePlayerAndRanking(long gameId, String winningTeam) {
        List<InGamePlayer> playerList = redisRepository.getInGamePlayerList(gameId);

        for (InGamePlayer inGamePlayer : playerList) {
            long memberId = inGamePlayer.getMemberId();
            Player player = playerRepository.findByGameIdAndMemberId(gameId, memberId)
                    .orElseThrow(() -> new CustomWebSocketException(PLAYER_IS_NOT_EXIST));
            Ranking ranking = rankingRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new CustomWebSocketException(RANKING_IS_NOT_EXIST));

            if (player.getTeam().name().equals(winningTeam)) {
                player.winGame();
                ranking.saveWinScore();
            } else {
                player.loseGame();
                ranking.saveLoseScore();
            }
        }

        return playerList.stream()
                .map(PlayerDataDto::toDto)
                .collect(Collectors.toList());
    }


    private InGamePlayer getInGamePlayer(long gameId, long memberId) {
        return redisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }
}
