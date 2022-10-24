package com.a301.theknight.domain.player.entity.game.entity;

import com.a301.theknight.domain.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Game extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    private int sword;

    private int twin;

    private int shield;

    private int hand;

    private int capacity;

    @Builder
    public Game(String title, GameStatus gameStatus, int sword, int twin, int shield, int hand, int capacity){
        this.title = title;
        this.status = gameStatus;
        this.sword = sword;
        this.twin = twin;
        this.shield = shield;
        this.hand = hand;
        this.capacity = capacity;
    }
}
