package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamInfoData {
    private int currentAttackIndex;
    private GameOrderDto[] orderList;
    private long leaderId;
    private boolean selected;

    @Builder
    public TeamInfoData(int currentAttackIndex, GameOrderDto[] orderList, long leaderId) {
        this.currentAttackIndex = currentAttackIndex;
        this.orderList = orderList;
        this.leaderId = leaderId;
    }

    public void completeSelect() {
        selected = true;
    }

    public void updateCurrentAttackIndex(int index) {
        this.currentAttackIndex = index;
    }
}
