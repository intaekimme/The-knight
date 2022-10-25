package com.a301.theknight.domain.chatting.entity;

import com.a301.theknight.domain.common.entity.BaseTimeEntity;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Chatting extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChattingSet chattingSet;

    @Column(length = 1000, nullable = false)
    private String content;

    @Builder
    public Chatting(Member member, Game game, ChattingSet chattingSet, String content) {
        this.member = member;
        this.game = game;
        this.chattingSet = chattingSet;
        this.content = content;
    }
}
