package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
public class TeamInfoData {
    private int currentAttackIndex;
    private GameOrderDto[] orderList;
    private long leaderId;
    private boolean selected;

    public void completeSelect() {
        selected = true;
    }

    public void updateCurrentAttackIndex(int index) {
        this.currentAttackIndex = index;
    }
}
