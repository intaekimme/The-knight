package com.a301.theknight.domain.ranking.repository;

import com.a301.theknight.domain.ranking.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    Optional<Ranking> findByMemberId(long memberId);

    @Query(value = "SELECT rank " +
            "FROM (SELECT member_id, rank() OVER (ORDER BY score DESC) as rank " +
            "FROM ranking) r " +
            "WHERE member_id = :memberId", nativeQuery = true)
    int findMemberRanking(@Param("memberId") long memberId);
}
