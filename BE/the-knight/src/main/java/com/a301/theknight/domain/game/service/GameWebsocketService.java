package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameWebsocketService {

    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;

    public void modify(long memberId){

    }

}
