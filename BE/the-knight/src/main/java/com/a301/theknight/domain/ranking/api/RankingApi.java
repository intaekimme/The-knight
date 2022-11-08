package com.a301.theknight.domain.ranking.api;

import com.a301.theknight.domain.ranking.dto.RankingResponse;
import com.a301.theknight.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;

@RequiredArgsConstructor
@RestController
public class RankingApi {

    private final RankingService rankingService;

    @GetMapping("/api/ranking")
    public ResponseEntity<?> getMemberHistory(@Size(max = 45) @RequestParam(required = false) String keyword) {
        RankingResponse rankingResponse = rankingService.getRankings(keyword);
        return ResponseEntity.ok(rankingResponse);
    }
}
