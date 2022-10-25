package com.a301.theknight.domain.member.service;

import com.a301.theknight.domain.member.dto.GameHistoryDto;
import com.a301.theknight.domain.member.dto.MemberHistoryResponse;
import com.a301.theknight.domain.member.dto.MemberInfoResponse;
import com.a301.theknight.domain.member.dto.MemberUpdateRequest;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.ranking.entity.Ranking;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final RankingRepository rankingRepository;

    @Transactional
    public MemberInfoResponse getMemberInfo(long memberId) {
        Member member = getMember(memberId);
        Ranking ranking = rankingRepository.findByMemberId(memberId)
                .orElseGet(() -> Ranking.builder().build());

        return MemberInfoResponse.builder()
                .nickname(member.getNickname())
                .image(member.getImage())
                .ranking(rankingRepository.findMemberRanking(memberId))
                .score(ranking.getScore())
                .win(ranking.getWin())
                .lose(ranking.getLose())
                .build();
    }

    @Transactional
    public void updateMemberInfo(long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member member = getMember(memberId);

        member.updateInfo(memberUpdateRequest.getNickname(), memberUpdateRequest.getImage());
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다."));
    }

    @Transactional
    public void deleteMember(long memberId) {
        Member member = getMember(memberId);
        memberRepository.delete(member);
    }

    @Transactional
    public MemberHistoryResponse getMemberHistory(long memberId) {

        List<GameHistoryDto> historyDtoList = new ArrayList<>();

        //player 중 memberId와 결과가 null이 아닌 걸로 조회 (수정 날짜순으로 desc후 limit 10개)
        //해당 player 10개의 game을 순서대로 DTO에 채워줌
//        GameHistoryDto.builder()
//                .gameId()
//                .result()
//                .capacity()
//                .sword()
//                .twin()
//                .shield()
//                .hand()
//                .alliance()//game
//                .opposite()
//                .build();

        return new MemberHistoryResponse(historyDtoList);
    }
}
