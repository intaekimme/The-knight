package com.a301.theknight.domain.game.repository;

import com.a301.theknight.domain.game.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameCustomRepository {
    Page<Game> findGameList(String keyword, Pageable pageable);
}
