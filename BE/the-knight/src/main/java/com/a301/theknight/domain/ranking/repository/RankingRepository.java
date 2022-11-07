package com.a301.theknight.domain.ranking.repository;

import com.a301.theknight.domain.ranking.dto.RankingDto;
import com.a301.theknight.domain.ranking.entity.Ranking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT m.nickname, m.image, rank() OVER (ORDER BY r.score DESC) AS ranking, r.score " +
            "FROM ranking AS r LEFT JOIN member AS m " +
            "ON r.member_id = m.id " +
            "ORDER BY ranking ASC",
            countQuery = "SELECT count(*) " +
                    "FROM ranking AS r LEFT JOIN member AS m " +
                    "ON r.member_id = m.id ",
            nativeQuery = true)
    Page<RankingDto> getRankingList(Pageable pageable);

    @Query(value = "SELECT m.nickname, m.image, rank() OVER (ORDER BY r.score DESC) AS ranking, r.score " +
            "FROM ranking AS r LEFT JOIN member AS m " +
            "ON r.member_id = m.id " +
            "WHERE m.nickname LIKE %:keyword% " +
            "ORDER BY ranking ASC",
            countQuery = "SELECT count(*) " +
                    "FROM ranking AS r LEFT JOIN member AS m " +
                    "ON r.member_id = m.id " +
                    "WHERE m.nickname LIKE %:keyword%",
            nativeQuery = true)
    Page<RankingDto> getRankingListByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
