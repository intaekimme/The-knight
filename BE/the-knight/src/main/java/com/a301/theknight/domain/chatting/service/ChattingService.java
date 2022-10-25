package com.a301.theknight.domain.chatting.service;

import com.a301.theknight.domain.chatting.dto.ChattingRequest;
import com.a301.theknight.domain.chatting.dto.ChattingResponse;
import com.a301.theknight.domain.chatting.entity.Chatting;
import com.a301.theknight.domain.chatting.repository.ChattingRepository;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
                .chattingSet(chatting.getChattingSet().name())
                .content(chatting.getContent()).build();
    }

    private Game getGame(long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("해당 게임이 존재하지 않습니다."));
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다."));
    }
}
