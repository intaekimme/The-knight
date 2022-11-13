package com.a301.theknight.domain.game.repository;

import com.a301.theknight.domain.game.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long>, GameCustomRepository {
    @EntityGraph(attributePaths = {"players"})
    Page<Game> findByTitleIsContaining(String keyword, Pageable pageable);

    @Query("select distinct g from Game g left join fetch g.players")
    List<Game> findAllByFetchJoin();

    @Query("select g from Game g join fetch g.players where g.id = :id")
    Optional<Game> findByIdFetchJoin(@Param("id") long gameId);

    @Query("SELECT g FROM Game g " +
            "JOIN FETCH g.players " +
            "WHERE g.id = :gameId ")
    Optional<Game> findGameWithPlayers(@Param("gameId") long gameId);
}
