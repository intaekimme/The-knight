package com.a301.theknight.domain.member.api;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.member.dto.MemberHistoryResponse;
import com.a301.theknight.domain.member.dto.MemberInfoResponse;
import com.a301.theknight.domain.member.dto.MemberUpdateRequest;
import com.a301.theknight.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@LoginMemberId long memberId) {
        return ResponseEntity.ok(memberService.getMemberInfo(memberId));
    }

    @PatchMapping("/members")
    public ResponseEntity<?> updateMemberInfo(@Valid @RequestBody MemberUpdateRequest memberUpdateRequest,
                                              @LoginMemberId long memberId) {
        memberService.updateMemberInfo(memberId, memberUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members")
    public ResponseEntity<?> deleteMember(@LoginMemberId long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    public ResponseEntity<?> getMemberHistory(@LoginMemberId long memberId) {
        MemberHistoryResponse memberHistoryResponse = memberService.getMemberHistory(memberId);
        return ResponseEntity.ok(memberHistoryResponse);
    }
}
