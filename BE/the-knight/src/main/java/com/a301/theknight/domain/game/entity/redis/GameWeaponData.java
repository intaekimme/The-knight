package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.Weapon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameWeaponData {
    private int sword;
    private int twin;
    private int shield;
    private int hand;

    public static GameWeaponData toWeaponData(Game game) {
        return GameWeaponData.builder()
                .sword(game.getSword())
                .shield(game.getShield())
                .twin(game.getTwin())
                .hand(game.getHand()).build();
    }

    public boolean canTakeWeapon(Weapon weapon) {
        if (Weapon.SWORD.equals(weapon) && sword > 0) {
            return true;
        } else if (Weapon.TWIN.equals(weapon) && twin > 0) {
            return true;
        } else if (Weapon.SHIELD.equals(weapon) && shield > 0) {
            return true;
        } else {
            return Weapon.HAND.equals(weapon) && hand > 0;
        }
    }

    public void choiceWeapon(Weapon weapon) {
        if (Weapon.SWORD.equals(weapon)) {
            sword--;
        } else if (Weapon.TWIN.equals(weapon)) {
            twin--;
        } else if (Weapon.SHIELD.equals(weapon)) {
            shield--;
        } else if (Weapon.HAND.equals(weapon)) {
            hand--;
        }
    }

    public void returnWeapon(Weapon weapon) {
        if (Weapon.SWORD.equals(weapon)) {
            sword++;
        } else if (Weapon.TWIN.equals(weapon)) {
            twin++;
        } else if (Weapon.SHIELD.equals(weapon)) {
            shield++;
        } else if (Weapon.HAND.equals(weapon)) {
            hand++;
        }
    }

    public boolean notAllSelected() {
        return sword != 0 || twin != 0 || shield != 0 || hand != 0;
    }
}
