package com.a301.theknight.domain.player.repository;

import com.a301.theknight.domain.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("SELECT COUNT(p) FROM Player p WHERE p.game.id = :gameId")
    long countPlayers(@Param("gameId") Long gameId);
}
