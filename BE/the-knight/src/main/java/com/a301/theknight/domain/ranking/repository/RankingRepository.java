package com.a301.theknight.domain.ranking.repository;

import com.a301.theknight.domain.ranking.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
}
