package com.a301.theknight.domain.player.repository;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long>, PlayerCustomRepository {
    Optional<Player> findByMember(Member member);
    Optional<Player> findByGameAndMember(Game game, Member member);

    @Query("select p from Player p where p.member.id = :memberId and p.game.id = :gameId")
    Optional<Player> findByGameIdAndMemberId(@Param("gameId")long gameId, @Param("memberId") long memberId);
}
