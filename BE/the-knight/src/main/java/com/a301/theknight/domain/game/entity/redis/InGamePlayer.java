package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.player.entity.Team;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InGamePlayer {
    private Long memberId;
    private String nickname;
    private String image;
    private Team team;
    private int leftCount;
    private int rightCount;
    private Weapon leftWeapon;
    private Weapon rightWeapon;
    private int order;
    private boolean dead;
    private boolean leader;

    @Builder
    public InGamePlayer(Long memberId, String nickname, String image, Team team, int leftCount, int rightCount, boolean leader) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.image = image;
        this.team = team;
        this.leftCount = leftCount;
        this.rightCount = rightCount;
        this.leader = leader;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean canTakeWeapon() {
        return leftWeapon == null || rightWeapon == null;
    }

    public void choiceWeapon(Weapon weapon, GameWeaponData weaponsData) {
        boolean isLeft = leftWeapon == null;
        setWeapon(weapon, isLeft);
        weaponsData.choiceWeapon(weapon);
    }

    public void randomChoiceWeapon(Weapon weapon) {
        boolean isLeft = leftWeapon == null;
        setWeapon(weapon, isLeft);
    }

    public void cancelWeapon(Hand deleteHand, GameWeaponData weaponsData) {
        boolean isLeft = Hand.LEFT.equals(deleteHand);
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

    public boolean isFullSelectWeapon() {
        return leftWeapon != null && rightWeapon != null;
    }

    public void death() {
        dead = true;
    }

    public void changeCount(int resultCount, Hand defendHand) {
        if (Hand.LEFT.equals(defendHand)) {
            leftCount = resultCount;
        } else {
            rightCount = resultCount;
        }
    }

    public void clearWeapon() {
        leftWeapon = null;
        rightWeapon = null;
    }
}
