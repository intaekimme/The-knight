package com.a301.theknight.domain.ranking.service;

import com.a301.theknight.domain.ranking.dto.RankingDto;
import com.a301.theknight.domain.ranking.dto.RankingResponse;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import org.springframework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RankingService {

    private final RankingRepository rankingRepository;

    @Transactional
    public RankingResponse getRankings(@Nullable String keyword) {
        List<RankingDto> rankings = StringUtils.hasText(keyword)
                ? rankingRepository.getRankingListByKeyword(keyword)
                : rankingRepository.getRankingList();
        return new RankingResponse(rankings);
    }
}
