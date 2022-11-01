package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.player.entity.Team;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class InGamePlayer implements Serializable {
    private Long memberId;
    private String nickname;
    private String image;
    private Team team;
    private int leftCount;
    private int rightCount;
    private Weapon leftWeapon;
    private Weapon rightWeapon;
    private int order;
    private boolean isDead;
    private boolean isLeader;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean canTakeWeapon() {
        return leftWeapon == null || rightWeapon == null;
    }

    public void choiceWeapon(Weapon weapon, GameWeaponData weaponsData) {
        boolean isLeft = leftWeapon == null ? true : false;
        setWeapon(weapon, isLeft);
        weaponsData.choiceWeapon(weapon);
    }

    public void cancelWeapon(boolean isLeft, GameWeaponData weaponsData) {
        weaponsData.returnWeapon(isLeft ? leftWeapon : rightWeapon);
        if (isLeft) {
            leftWeapon = rightWeapon;
        }
        rightWeapon = null;
    }

    private void setWeapon(Weapon weapon, boolean isLeft) {
        if (isLeft) {
            leftWeapon = weapon;
            return;
        }
        rightWeapon = weapon;
    }

    public void saveOrder(int orderNumber) {
        order = orderNumber;
    }
}
