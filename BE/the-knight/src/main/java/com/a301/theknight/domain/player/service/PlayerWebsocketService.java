package com.a301.theknight.domain.player.service;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.dto.PlayerEntryResponse;
import com.a301.theknight.domain.player.dto.PlayerReadyResponse;
import com.a301.theknight.domain.player.dto.PlayerTeamResponse;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class PlayerWebsocketService {

    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public PlayerEntryResponse entry(long gameId, long memberId){
        Game entryGame = getGame(gameId);
        if(!isWaiting(entryGame)){
            //TODO 커스텀 예외처리로 refactoring
            throw new RuntimeException("대기중인 게임이 아닙니다.");
        }
        if(!isEnterPossible(entryGame)){
            //TODO 커스텀 예외처리로 refactoring
            throw new RuntimeException("허용 인원을 초과했습니다.");
        }
        Member entryMember = getMember(memberId);
        Player entryPlayer = Player.builder().member(entryMember).game(entryGame).build();

        return PlayerEntryResponse.builder().playerId(entryPlayer.getId())
                .nickname(entryMember.getNickname())
                .image(entryMember.getImage())
                .build();
    }

    @Transactional
    public long exit(long gameId, long memberId){
        Game exitGame = getGame(gameId);

        if(!isWaiting(exitGame)){
            //TODO 커스텀 예외처리로 refactoring
            throw new RuntimeException("대기중인 게임이 아닙니다.");
        }
        Member findMember = getMember(memberId);
        Player exitPlayer = getPlayer(findMember);
        exitPlayer.exitGame();

        long exitPlayerId = exitPlayer.getId();
        playerRepository.delete(exitPlayer);

        return exitPlayerId;
    }

    @Transactional
    public PlayerTeamResponse team(long gameId, long memberId, String team){
        Game findGame = getGame(gameId);
        Member findMember = getMember(memberId);

        Player findPlayer = getPlayer(findGame, findMember);
        //TODO NotNull 유효성 검사
        findPlayer.selectTeam(Team.A.name().equals(team) ? Team.A : Team.B);

        return PlayerTeamResponse.builder()
                .playerId(findPlayer.getId())
                .team(findPlayer.getTeam().name())
                .build();
    }

    @Transactional
    public PlayerReadyResponse ready(long gameId, long memberId, boolean isReady){
        Game findGame = getGame(gameId);
        Member findMember = getMember(memberId);

        Player readyPlayer = getPlayer(findGame, findMember);

        readyPlayer.ready(isReady);

        if(!isOwner(findGame, readyPlayer)){
            return PlayerReadyResponse.builder()
                    .playerId(readyPlayer.getId())
                    .readyStatus(readyPlayer.isReady())
                    .build();
        }else{
            //TODO 방장일 경우 게임 시작 가능 여부 로직 처리
            return null;
        }
    }

    private Member getMember(long memberId) {
        //TODO 커스텀 예외처리로 refactoring
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
    }

    private Game getGame(long gameId) {
        //TODO 커스텀 예외처리로 refactoring
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("해당 게임이 존재하지 않습니다."));
    }

    private Player getPlayer(Member member){
        //TODO 커스텀 예외처리로 refactoring
        return playerRepository.findByMember(member)
                .orElseThrow(() -> new NoSuchElementException("해당 플레이어가 존재하지 않습니다."));
    }

    private Player getPlayer(Game game, Member member){
        //TODO 커스텀 예외처리로 refactoring
        return playerRepository.findByGameAndMember(game, member)
                .orElseThrow(() -> new NoSuchElementException("해당 플레이어가 존재하지 않습니다."));
    }

    private boolean isWaiting(Game game){
        return game.getStatus() == GameStatus.WAITING;
    }
    private boolean isEnterPossible(Game game){
        return game.getCapacity() > game.getPlayers().size();
    }

    private boolean isOwner(Game game, Player player){
        return game.getPlayers().get(0).equals(player);
    }
}
