package com.a301.theknight.domain.chatting.service;

import com.a301.theknight.domain.chatting.dto.ChattingRequest;
import com.a301.theknight.domain.chatting.dto.ChattingResponse;
import com.a301.theknight.domain.chatting.entity.Chatting;
import com.a301.theknight.domain.chatting.repository.ChattingRepository;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.global.error.errorcode.GameErrorCode;
import com.a301.theknight.global.error.errorcode.MemberErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
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

        //TODO : Optional<Player>를 찾고, 해당 회원이 다른 팀으로 채팅을 보내는 경우 예외처리
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

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomRestException(GameErrorCode.GAME_IS_NOT_EXIST));
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomRestException(MemberErrorCode.MEMBER_IS_NOT_EXIST));
    }
}
