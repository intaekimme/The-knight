package com.a301.theknight.domain.ranking.api;

import com.a301.theknight.domain.member.dto.MemberHistoryResponse;
import com.a301.theknight.domain.member.dto.MemberInfoResponse;
import com.a301.theknight.domain.member.dto.MemberUpdateRequest;
import com.a301.theknight.domain.member.service.MemberService;
import com.a301.theknight.domain.ranking.dto.RankingResponse;
import com.a301.theknight.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class RankingApi {

    private final RankingService rankingService;

    @GetMapping("/api/ranking")
    public ResponseEntity<?> getMemberHistory(@RequestParam String keyword) {
        RankingResponse rankingResponse = new RankingResponse();
        return ResponseEntity.ok(rankingResponse);
    }
}
