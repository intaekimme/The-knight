package com.a301.theknight.domain.player.entity;

import com.a301.theknight.domain.common.entity.BaseTimeEntity;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameResult;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Player extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    @Enumerated(EnumType.STRING)
    private Team team;

    @Enumerated(EnumType.STRING)
    private GameResult result;

    private int leftCount;

    private int rightCount;

    @Enumerated(EnumType.STRING)
    private Weapon leftWeapon;

    @Enumerated(EnumType.STRING)
    private Weapon rightWeapon;

    private int sequence;

    private boolean isDead;

    private boolean isReady;

    private boolean isLeader;

    @Builder
    public Player(Member member, Game game) {
        this.member = member;
        this.game = game;
        this.team = Team.A;
        game.getPlayers().add(this);
    }

    public void exitGame(){
        if(this.game  != null){
            this.game.getPlayers().remove(this);
        }
    }

    public void selectTeam(Team team) {
        this.team = team;
    }

    public void winGame() {
        result = GameResult.WIN;
    }

    public void loseGame() {
        result = GameResult.LOSE;
    }

    public void ready(boolean isReady) { this.isReady = isReady; }

    public void becomeLeader() {
        isLeader = true;
    }
}
