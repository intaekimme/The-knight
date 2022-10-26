package com.a301.theknight.domain.player.service;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.player.dto.PlayerEntryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class PlayerWebsocketService {

    private final GameRepository gameRepository;

    @Transactional
    public PlayerEntryResponse entry(long gameId, long memberId){
        Game findGame = getGame(gameId);
        if(!isWaiting(findGame)){
            throw new RuntimeException("대기중인 게임이 아닙니다.");
        }else{

        }
        return null;
    }


    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("해당 게임이 존재하지 않습니다."));
    }

    private boolean isWaiting(Game game){
        return game.getStatus() == GameStatus.WAITING;
    }
}
