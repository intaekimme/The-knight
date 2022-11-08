package com.a301.theknight.domain.ranking.repository;

import com.a301.theknight.domain.ranking.dto.RankingDto;
import com.a301.theknight.domain.ranking.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    Optional<Ranking> findByMemberId(long memberId);

    @Query(value = "SELECT rank " +
            "FROM (SELECT member_id, rank() OVER (ORDER BY score DESC) as rank " +
            "FROM ranking) r " +
            "WHERE member_id = :memberId", nativeQuery = true)
    Long findMemberRanking(@Param("memberId") long memberId);

    @Query(value = "SELECT m.nickname, m.image, r.ranking, r.score " +
            "FROM (SELECT member_id, rank() OVER (ORDER BY score DESC) as ranking, score " +
            "       FROM ranking) AS r " +
            "LEFT JOIN member AS m " +
            "ON r.member_id = m.id " +
            "ORDER BY ranking ASC", nativeQuery = true)
    List<RankingDto> getRankingList();

    @Query(value = "SELECT m.nickname, m.image, r.ranking, r.score " +
            "FROM (SELECT member_id, rank() OVER (ORDER BY score DESC) as ranking, score " +
            "       FROM ranking) AS r " +
            "LEFT JOIN member AS m " +
            "ON r.member_id = m.id " +
            "WHERE m.nickname LIKE %:keyword% " +
            "ORDER BY ranking ASC", nativeQuery = true)
    List<RankingDto> getRankingListByKeyword(@Param("keyword") String keyword);
}
