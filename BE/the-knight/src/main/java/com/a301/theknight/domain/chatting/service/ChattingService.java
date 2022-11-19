package com.a301.theknight.domain.chatting.service;

import com.a301.theknight.domain.chatting.dto.ChattingRequest;
import com.a301.theknight.domain.chatting.dto.ChattingResponse;
import com.a301.theknight.domain.chatting.entity.Chatting;
import com.a301.theknight.domain.chatting.entity.ChattingSet;
import com.a301.theknight.domain.chatting.repository.ChattingRepository;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.global.error.errorcode.ChattingErrorCode;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.MemberErrorCode;
import com.a301.theknight.global.error.errorcode.PlayerErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChattingService {

    private final ChattingRepository chattingRepository;
    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;

    @Transactional
    public ChattingResponse makeResponse(long memberId, long gameId, ChattingRequest chattingRequest) {
        Member member = getMember(memberId);
        Game game = getGame(gameId);

        sendCheckOtherTeam(member, game, chattingRequest.getChattingSet());

        Chatting chatting = chattingRepository.save(Chatting.builder()
                .member(member)
                .game(game)
                .chattingSet(chattingRequest.getChattingSet())
                .content(chattingRequest.getContent()).build());

        return ChattingResponse.builder()
                .memberId(memberId)
                .nickname(member.getNickname())
                .chattingSet(chatting.getChattingSet().name())
                .content(chatting.getContent()).build();
    }

    private void sendCheckOtherTeam(Member member, Game game, ChattingSet chattingSet) {
        if (ChattingSet.ALL.equals(chattingSet)) {
            return;
        }
        Player player = game.getPlayers().stream()
                .filter(p -> p.getMember().getId().equals(member.getId()))
                .findFirst()
                .orElseThrow(() -> new CustomWebSocketException(PlayerErrorCode.PLAYER_IS_NOT_EXIST));

        if (isOtherTeam(chattingSet, player)) {
            throw new CustomWebSocketException(ChattingErrorCode.CAN_NOT_SEND_OTHER_TEAM);
        }
    }

    private boolean isOtherTeam(ChattingSet chattingSet, Player player) {
        return !player.getTeam().name().equals(chattingSet.name());
    }

    private Game getGame(long gameId) {
        return gameRepository.findByIdFetchJoin(gameId)
                .orElseThrow(() -> new CustomRestException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomRestException(MemberErrorCode.MEMBER_IS_NOT_EXIST));
    }
}
