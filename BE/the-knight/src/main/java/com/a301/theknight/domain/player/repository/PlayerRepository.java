package com.a301.theknight.domain.player.repository;

import com.a301.theknight.domain.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
