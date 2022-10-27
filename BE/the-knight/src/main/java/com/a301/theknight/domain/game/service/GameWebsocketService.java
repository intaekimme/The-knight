package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.GameModifyRequest;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class GameWebsocketService {

    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;

    private final PlayerRepository playerRepository;

    @Transactional
    public void modify(long gameId, long memberId, GameModifyRequest gameModifyRequest){
        Game findGame = getGame(gameId);

        if(findGame.getStatus() == GameStatus.WAITING){
            if(isOwner(findGame, memberId)){
                findGame.ModifyGame(
                        gameModifyRequest.getTitle(),
                        gameModifyRequest.getCapacity(),
                        gameModifyRequest.getSword(),
                        gameModifyRequest.getTwin(),
                        gameModifyRequest.getShield(),
                        gameModifyRequest.getHand()
                );
            }else{
                throw new RuntimeException("수정은 방장만 가능합니다.");
            }

        }else{
            throw new RuntimeException("대기 상태가 아닙니다.");
        }
    }

    @Transactional
    public void delete(long gameId, long memberId){
        Game findGame = getGame(gameId);
        if(isOwner(findGame, memberId)){
            playerRepository.deleteAll(findGame.getPlayers());
            gameRepository.delete(findGame);
        }else{
            throw new RuntimeException("방 삭제는 방장만 가능합니다.");
        }
    }


    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("해당 게임이 존재하지 않습니다."));
    }

    private boolean isOwner(Game game, long memberId){
        Player owner = game.getPlayers().get(0);
        return owner.getMember().getId().equals(memberId);
    }
}
