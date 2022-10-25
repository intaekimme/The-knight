package com.a301.theknight.domain.game.dto;

import com.a301.theknight.domain.game.entity.Weapon;

public class InGame {
    private Long memberId;
    private int leftCount;
    private int rightCount;
    private Weapon leftWeapon;
    private Weapon rightWeapon;
    private int order;
    private boolean isDead;
}
