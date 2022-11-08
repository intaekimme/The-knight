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
        //TODO: 쿼리 테스트 필요!!
        List<RankingDto> rankings = StringUtils.hasText(keyword)
                ? rankingRepository.getRankingList()
                : rankingRepository.getRankingListByKeyword(keyword);

        return new RankingResponse(rankings);
    }
}
