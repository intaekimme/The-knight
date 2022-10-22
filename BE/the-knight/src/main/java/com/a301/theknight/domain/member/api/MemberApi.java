package com.a301.theknight.domain.member.api;

import com.a301.theknight.domain.member.dto.MemberHistoryResponse;
import com.a301.theknight.domain.member.dto.MemberInfoResponse;
import com.a301.theknight.domain.member.dto.MemberUpdateRequest;
import com.a301.theknight.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberApi {

    private final MemberService memberService;
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members")
    public ResponseEntity<?> getMemberInfo() {
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
        return ResponseEntity.ok(memberInfoResponse);
    }

    @PatchMapping("/members")
    public ResponseEntity<?> updateMemberInfo(@RequestBody MemberUpdateRequest memberUpdateRequest) {
        return null;
    }

    @DeleteMapping("/members")
    public ResponseEntity<?> deleteMember() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    public ResponseEntity<?> getMemberHistory() {
        MemberHistoryResponse memberHistoryResponse = new MemberHistoryResponse();
        return ResponseEntity.ok(memberHistoryResponse);
    }
}
