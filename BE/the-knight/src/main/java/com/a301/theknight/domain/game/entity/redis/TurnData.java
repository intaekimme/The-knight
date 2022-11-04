package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.service.GameDefenseService;
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

    public void recordDefenseTurn(GameDefenseRequest gameDefenseRequest){
        this.defenderId = gameDefenseRequest.getDefender().getId();
        this.defendData = new DefendData(gameDefenseRequest.getHand());
    }

    public void checkLyingAttack(InGamePlayer attacker){
         this.lyingAttack = (this.attackData.getAttackHand().name().equals("LEFT") && (!this.attackData.getWeapon().name().equals(attacker.getLeftWeapon().name()))) ||
                 (this.attackData.getAttackHand().name().equals("RIGHT") && (!this.attackData.getWeapon().name().equals(attacker.getRightWeapon().name())));
    }

    public void checkLyingDefense(InGamePlayer defender){
        this.lyingDefend = (this.defendData.getDefendHand().name().equals("LEFT") && (defender.getLeftWeapon().equals(Weapon.SHIELD))) ||
                (this.defendData.getDefendHand().name().equals("RIGHT") && (defender.getRightWeapon().equals(Weapon.SHIELD)));
    }
}
