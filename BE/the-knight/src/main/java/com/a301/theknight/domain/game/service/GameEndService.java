package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.end.GameEndDto;
import com.a301.theknight.domain.game.dto.end.PlayerWeaponDto;
import com.a301.theknight.domain.game.dto.end.response.EndResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.domain.ranking.entity.Ranking;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import com.a301.theknight.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.a301.theknight.global.error.errorcode.GameErrorCode.GAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.MemberErrorCode.MEMBER_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.PlayerErrorCode.PLAYER_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.RankingErrorCode.RANKING_IS_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class GameEndService {

    private final GameRedisRepository gameRedisRepository;
    private final GameRepository gameRepository;
    private final RankingRepository rankingRepository;
    private final PlayerRepository playerRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public GameEndDto gameEnd(long gameId) {

        // GameEnd 비즈니스 로직 수행
        // 1. player update(result만)
        // 2. Ranking 점수 갱신
        // 3. 게임 상태 End로 update
        // 4. GameEndDto 채워서 리턴

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new CustomException(GAME_IS_NOT_EXIST));
        game.changeStatus(GameStatus.END);
        InGame inGame = gameRedisRepository.getInGame(gameId).orElseThrow(() -> new CustomException(INGAME_IS_NOT_EXIST));

        long LeaderAId = inGame.getTeamAInfo().getLeaderId();
        long LeaderBId = inGame.getTeamAInfo().getLeaderId();
        InGamePlayer LeaderA = gameRedisRepository.getInGamePlayer(gameId, LeaderAId).orElseThrow(() -> new CustomException(INGAME_PLAYER_IS_NOT_EXIST));

        String losingTeam = LeaderA.isDead() ? "A" : "B";
        List<PlayerWeaponDto> players = new ArrayList<>();

        GameOrderDto[] orderList = inGame.getTeamAInfo().getOrderList();
        boolean isWin = losingTeam.equals("B");

        for (int i = 0; i < 2; i++) {

            for (GameOrderDto gameOrderDto : orderList) {
                long memberId = gameOrderDto.getMemberId();
                Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_IS_NOT_EXIST));
                Player player = playerRepository.findByGameAndMember(game, member).orElseThrow(() -> new CustomException(PLAYER_IS_NOT_EXIST));
                InGamePlayer inGamePlayer = gameRedisRepository.getInGamePlayer(gameId, memberId).orElseThrow(() -> new CustomException(INGAME_PLAYER_IS_NOT_EXIST));
                Ranking ranking = rankingRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(RANKING_IS_NOT_EXIST));

                if (isWin) {
                    player.winGame();
                    ranking.saveWinScore();

                } else {
                    player.loseGame();
                    ranking.saveLoseScore();
                }

                players.add(PlayerWeaponDto.builder().memberId(memberId).leftWeapon(inGamePlayer.getLeftWeapon().toString()).rightWeapon(inGamePlayer.getRightWeapon().toString()).build());
            }

            orderList = inGame.getTeamBInfo().getOrderList();
            isWin = !isWin;
        }
        EndResponse endResponseA = EndResponse.builder().isWin(isWin).losingTeam(losingTeam).losingLeaderId(isWin ? LeaderBId : LeaderAId).winningLeaderId(isWin ? LeaderAId : LeaderBId).players(players).build();
        EndResponse endResponseB = EndResponse.builder().isWin(!isWin).losingTeam(losingTeam).losingLeaderId(isWin ? LeaderBId : LeaderAId).winningLeaderId(isWin ? LeaderAId : LeaderBId).players(players).build();

        return GameEndDto.builder().endResponseA(endResponseA).endResponseB(endResponseB).build();
    }

}
