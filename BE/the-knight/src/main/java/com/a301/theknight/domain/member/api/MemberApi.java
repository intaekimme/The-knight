package com.a301.theknight.domain.member.api;

import com.a301.theknight.domain.member.dto.MemberHistoryResponse;
import com.a301.theknight.domain.member.dto.MemberInfoResponse;
import com.a301.theknight.domain.member.dto.MemberUpdateRequest;
import com.a301.theknight.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberApi {

    private final MemberService memberService;

    @DeleteMapping("/oauth2/authorization/google")
    public ResponseEntity<?> login(@RequestParam String redirect_uri) {
        return null;
    }

    @DeleteMapping("/api/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/members")
    public ResponseEntity<?> getMemberInfo() {
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
        return ResponseEntity.ok(memberInfoResponse);
    }

    @PatchMapping("/api/members")
    public ResponseEntity<?> updateMemberInfo(@RequestBody MemberUpdateRequest memberUpdateRequest) {
        return null;
    }

    @DeleteMapping("/api/members")
    public ResponseEntity<?> deleteMember() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/history")
    public ResponseEntity<?> getMemberHistory() {
        MemberHistoryResponse memberHistoryResponse = new MemberHistoryResponse();
        return ResponseEntity.ok(memberHistoryResponse);
    }
}
