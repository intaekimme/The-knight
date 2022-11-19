package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.entity.Weapon;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TurnData {
    private int turn;
    private long attackerId;
    private long defenderId;
    private AttackData attackData;
    private DefendData defenseData;
    private boolean lyingAttack;
    private boolean lyingDefense;
    private DoubtData doubtData;

    public void recordAttackData(InGamePlayer attacker, InGamePlayer defender, GameAttackRequest gameAttackRequest){
        attackerId = attacker.getMemberId();
        attackData.setWeapon(gameAttackRequest.getWeapon());
        attackData.setAttackHand(gameAttackRequest.getHand());
        defenderId = defender.getMemberId();
    }

    public void recordDefenseData(InGamePlayer defender, GameDefenseRequest gameDefenseRequest){
        defenderId = defender.getMemberId();
        defenseData.setDefendHand(gameDefenseRequest.getHand());
        defenseData.setShieldCount(Hand.LEFT.equals(gameDefenseRequest.getHand()) ?
                defender.getLeftCount() : defender.getRightCount());
    }

    public void addDoubtPassCount() {
        doubtData.addPassCount();
    }

    public void clearAttackData() {
        attackerId = 0L;
        attackData.setWeapon(null);
        attackData.setAttackHand(null);
        lyingAttack = false;
    }

    public void clearDefenseData() {
        defenderId = 0L;
        defenseData.setDefendHand(null);
        defenseData.setShieldCount(0);
        lyingDefense = false;
    }

    public void recordAttackLying(InGamePlayer attacker){
        Weapon attackerWeapon = Hand.LEFT.equals(attackData.getAttackHand()) ? attacker.getLeftWeapon() : attacker.getRightWeapon();
        lyingAttack = !attackerWeapon.equals(attackData.getWeapon());
    }

    public void recordDefenseLying(InGamePlayer defender){
        Weapon defenderWeapon = Hand.LEFT.equals(defenseData.getDefendHand()) ? defender.getLeftWeapon() : defender.getRightWeapon();
        lyingDefense = !Weapon.SHIELD.equals(defenderWeapon);
    }

    public void setDoubtData(DoubtData doubtData) {
        this.doubtData = doubtData;
    }

    public void addTurn() {
        turn++;
    }

    public void clearDoubtData() {
        doubtData.setSuspectId(0L);
        doubtData.setSuspectedId(0L);
        doubtData.setDoubtStatus(null);
        doubtData.setDoubtHand(null);
        doubtData.setDoubtSuccess(false);
        doubtData.setDeadLeader(false);
        doubtData.setDoubtPassCount(0);
    }
}
