package com.a301.theknight.domain.ranking.service;

import com.a301.theknight.domain.ranking.dto.RankingDto;
import com.a301.theknight.domain.ranking.dto.RankingResponse;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import io.micrometer.core.instrument.util.StringUtils;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RankingService {

    private final RankingRepository rankingRepository;

    @Transactional
    public RankingResponse getRankings(@Nullable String keyword, Pageable pageable) {
        //TODO: 쿼리 테스트 필요!!
        Page<RankingDto> rankings = StringUtils.isBlank(keyword)
                ? rankingRepository.getRankingList(pageable)
                : rankingRepository.getRankingListByKeyword(keyword, pageable);

        return new RankingResponse(rankings.getContent());
    }
}
