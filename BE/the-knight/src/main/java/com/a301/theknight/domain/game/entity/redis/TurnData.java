package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class TurnData implements Serializable {
    private long attackerId;
    private long defenderId;
    private AttackData attackData;
    private DefendData defendData;
    private boolean lyingAttack;
    private boolean lyingDefend;

    public void recordAttackTurn(GameAttackRequest gameAttackRequest){
        this.attackerId = gameAttackRequest.getAttacker().getId();
        this.attackData = new AttackData(gameAttackRequest.getWeapon(), gameAttackRequest.getHand());
        this.defenderId = gameAttackRequest.getDefender().getId();
    }

    public void checkLyingAttack(InGamePlayer attacker){
         this.lyingAttack = (this.attackData.getAttackHand().name().equals("LEFT") && (!this.attackData.getWeapon().name().equals(attacker.getLeftWeapon().name()))) ||
                 (this.attackData.getAttackHand().name().equals("RIGHT") && (!this.attackData.getWeapon().name().equals(attacker.getRightWeapon().name())));
    }
}
