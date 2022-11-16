package com.a301.theknight.domain.game.entity;

import com.a301.theknight.domain.common.entity.BaseTimeEntity;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @OneToMany(mappedBy = "game")
    private List<Player> players = new ArrayList<>();

    private int sword;

    private int twin;

    private int shield;

    private int hand;

    private int capacity;

    private boolean canStart;

    @Builder
    public Game(String title, int sword, int twin, int shield, int hand, int capacity){
        this.title = title;
        this.status = GameStatus.WAITING;
        this.sword = sword;
        this.twin = twin;
        this.shield = shield;
        this.hand = hand;
        this.capacity = capacity;
    }

    //TODO  test 용도 추후 삭제
    public Game(long id, String title, int sword, int twin, int shield, int hand, int capacity){
        this.id = id;
        this.title = title;
        this.status = GameStatus.WAITING;
        this.sword = sword;
        this.twin = twin;
        this.shield = shield;
        this.hand = hand;
        this.capacity = capacity;
    }

    public void changeStatus(GameStatus gameStatus) {
        this.status = gameStatus;
    }

    public void ModifyGame(String title, int capacity, int sword, int twin, int shield, int hand){
        this.title = title;
        this.capacity = capacity;
        this.sword = sword;
        this.twin = twin;
        this.shield = shield;
        this.hand = hand;
    }

    public void completeReady() {
        canStart = true;
    }

    public Optional<Player> getTeamLeader(Team team) {
        return players.stream()
                .filter(player -> player.getTeam().equals(team) && player.isLeader())
                .findFirst();
    }

    public Player getOwner() {
        return players.stream()
                .filter(Player::isOwner)
                .findFirst().get();
    }
}
