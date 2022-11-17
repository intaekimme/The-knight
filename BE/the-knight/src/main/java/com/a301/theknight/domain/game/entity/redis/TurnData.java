package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.entity.Weapon;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TurnData {
    private long attackerId;
    private long defenderId;
    private AttackData attackData;
    private DefendData defendData;
    private boolean lyingAttack;
    private boolean lyingDefend;
    private DoubtData doubtData;

    public void recordAttackTurn(InGamePlayer attacker, InGamePlayer defender, GameAttackRequest gameAttackRequest){
        attackerId = attacker.getMemberId();
        attackData.setWeapon(gameAttackRequest.getWeapon());
        attackData.setAttackHand(gameAttackRequest.getHand());
        defenderId = defender.getMemberId();
    }

    public void recordDefenseTurn(InGamePlayer defender, GameDefenseRequest gameDefenseRequest){
        defenderId = defender.getMemberId();
        defendData.setDefendHand(gameDefenseRequest.getHand());
        defendData.setShieldCount(Hand.LEFT.equals(gameDefenseRequest.getHand()) ?
                defender.getLeftCount() : defender.getRightCount());
    }

    public void checkLyingAttack(InGamePlayer attacker){
         this.lyingAttack = (this.attackData.getAttackHand().name().equals("LEFT") && (!this.attackData.getWeapon().name().equals(attacker.getLeftWeapon().name()))) ||
                 (this.attackData.getAttackHand().name().equals("RIGHT") && (!this.attackData.getWeapon().name().equals(attacker.getRightWeapon().name())));
    }

    public void checkLyingDefense(InGamePlayer defender){
        this.lyingDefend = (this.defendData.getDefendHand().name().equals("LEFT") && (!defender.getLeftWeapon().equals(Weapon.SHIELD))) ||
                (this.defendData.getDefendHand().name().equals("RIGHT") && (!defender.getRightWeapon().equals(Weapon.SHIELD)));
    }

    public void setDoubtData(DoubtData doubtData) {
        this.doubtData = doubtData;
    }
}
