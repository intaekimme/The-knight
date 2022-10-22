package com.a301.theknight.domain.game.repository;

import com.a301.theknight.domain.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
