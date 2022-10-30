package com.a301.theknight.domain.game.dto;

import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.player.entity.Team;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class InGame implements Serializable {
    private Long memberId;
    private String nickname;
    private Team team;
    private int leftCount;
    private int rightCount;
    private Weapon leftWeapon;
    private Weapon rightWeapon;
    private int order;
    private boolean isDead;
    private boolean isLeader;

    public void choiceWeapon(Weapon weapon, boolean isLeft) {

    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
