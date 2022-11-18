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
    private DefendData defendData;
    private boolean lyingAttack;
    private boolean lyingDefend;
    private DoubtData doubtData;

    public void recordAttackData(InGamePlayer attacker, InGamePlayer defender, GameAttackRequest gameAttackRequest){
        attackerId = attacker.getMemberId();
        attackData.setWeapon(gameAttackRequest.getWeapon());
        attackData.setAttackHand(gameAttackRequest.getHand());
        defenderId = defender.getMemberId();
    }

    public void recordDefenseData(InGamePlayer defender, GameDefenseRequest gameDefenseRequest){
        defenderId = defender.getMemberId();
        defendData.setDefendHand(gameDefenseRequest.getHand());
        defendData.setShieldCount(Hand.LEFT.equals(gameDefenseRequest.getHand()) ?
                defender.getLeftCount() : defender.getRightCount());
    }

    public void clearAttackData() {
        attackerId = 0L;
        attackData.setWeapon(null);
        attackData.setAttackHand(null);
        lyingAttack = false;
    }

    public void clearDefenseData() {
        defenderId = 0L;
        defendData.setDefendHand(null);
        defendData.setShieldCount(0);
        lyingDefend = false;
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
    }
}
