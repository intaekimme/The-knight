package com.a301.theknight.domain.game.dto.prepare;

import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

@Builder
@Data
public class PlayerDataDto {
    private long memberId;
    private String nickname;
    private String team;
    private int leftCount;
    private int rightCount;
    private int order;
    private Boolean isDead;
    private List<Weapon> weapons;

    public static PlayerDataDto toDto(InGamePlayer inGamePlayer) {
        return PlayerDataDto.builder()
                .memberId(inGamePlayer.getMemberId())
                .nickname(inGamePlayer.getNickname())
                .team(inGamePlayer.getTeam().name())
                .leftCount(inGamePlayer.getLeftCount())
                .rightCount(inGamePlayer.getRightCount())
                .order(inGamePlayer.getOrder())
                .isDead(inGamePlayer.isDead())
                .weapons(new ArrayList<>(asList(inGamePlayer.getLeftWeapon(), inGamePlayer.getRightWeapon())))
                .build();
    }

    public static PlayerDataDto toDto(InGamePlayer inGamePlayer, Team team) {
        List<Weapon> weapons = new ArrayList<>();
        weapons.add(team.equals(inGamePlayer.getTeam()) ? inGamePlayer.getLeftWeapon() : Weapon.HIDE);
        weapons.add(team.equals(inGamePlayer.getTeam()) ? inGamePlayer.getRightWeapon() : Weapon.HIDE);

        return PlayerDataDto.builder()
                .memberId(inGamePlayer.getMemberId())
                .nickname(inGamePlayer.getNickname())
                .team(inGamePlayer.getTeam().name())
                .leftCount(inGamePlayer.getLeftCount())
                .rightCount(inGamePlayer.getRightCount())
                .order(inGamePlayer.getOrder())
                .isDead(inGamePlayer.isDead())
                .weapons(weapons)
                .build();
    }

    public PlayerDataDto copyDto() {
        return PlayerDataDto.builder()
                .memberId(memberId)
                .nickname(nickname)
                .team(team)
                .leftCount(leftCount)
                .rightCount(rightCount)
                .order(order)
                .weapons(weapons)
                .build();
    }

    public void resetWeapons() {
        this.weapons = new ArrayList<>();
    }
}
