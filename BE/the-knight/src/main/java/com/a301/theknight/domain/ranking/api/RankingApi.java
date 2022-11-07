package com.a301.theknight.domain.ranking.api;

import com.a301.theknight.domain.ranking.dto.RankingResponse;
import com.a301.theknight.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RankingApi {

    private final RankingService rankingService;

    @GetMapping("/api/ranking")
    public ResponseEntity<?> getMemberHistory(@RequestParam(required = false) String keyword, Pageable pageable) {
        RankingResponse rankingResponse = rankingService.getRankings(keyword, pageable);
        return ResponseEntity.ok(rankingResponse);
    }
}
