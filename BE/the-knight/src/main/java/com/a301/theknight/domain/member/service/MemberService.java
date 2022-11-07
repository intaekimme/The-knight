package com.a301.theknight.domain.member.service;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.member.dto.*;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.domain.ranking.entity.Ranking;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import com.a301.theknight.global.error.errorcode.MemberErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final RankingRepository rankingRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public MemberInfoResponse getMemberInfo(long memberId) {
        Member member = getMember(memberId);
        Ranking ranking = rankingRepository.findByMemberId(memberId)
                .orElseGet(() -> Ranking.builder().build());

        Long rankingNumber = rankingRepository.findMemberRanking(memberId);

        return MemberInfoResponse.builder()
                .nickname(member.getNickname())
                .image(member.getImage())
                .ranking(rankingNumber == null ? 0L : rankingNumber)
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

    @Transactional
    public void deleteMember(long memberId) {
        Member member = getMember(memberId);
        memberRepository.delete(member);
    }

    @Transactional
    public MemberHistoryResponse getMemberHistory(long memberId) {
        List<GameHistoryDto> historyDtoList = new ArrayList<>();

        List<Player> players = playerRepository.findTenByMemberId(memberId);
        players.stream().forEach(player -> {
            Team myTeam = player.getTeam();
            Game game = player.getGame();

            List<MemberInfoDto> alliance = game.getPlayers().stream()
                    .filter(p -> myTeam.equals(p.getTeam()))
                    .map(p -> MemberInfoDto.builder()
                            .nickname(p.getMember().getNickname())
                            .image(p.getMember().getImage()).build())
                    .collect(Collectors.toList());

            List<MemberInfoDto> opposite = game.getPlayers().stream()
                    .filter(p -> !myTeam.equals(p.getTeam()))
                    .map(p -> MemberInfoDto.builder()
                            .nickname(p.getMember().getNickname())
                            .image(p.getMember().getImage()).build())
                    .collect(Collectors.toList());

            historyDtoList.add(GameHistoryDto.builder()
                .gameId(game.getId() == null ? 0 : game.getId())
                .result(player.getResult().name())
                .capacity(game.getCapacity())
                .sword(game.getSword())
                .twin(game.getTwin())
                .shield(game.getShield())
                .hand(game.getHand())
                .alliance(alliance)//game
                .opposite(opposite).build());
        });

        return new MemberHistoryResponse(historyDtoList);
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomRestException(MemberErrorCode.MEMBER_IS_NOT_EXIST));
    }
}
