package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.player.entity.Team;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class InGame implements Serializable {
    private TurnStatus status;
    private Team currentAttackTeam;
    private TeamInfoData teamAInfo;
    private TeamInfoData teamBInfo;
    private int maxMemberNum;
    private TurnData turnData;
    private int requestCount;

    public void initTurnData() {
        turnData = new TurnData();
    }

    public void choiceOrder(InGamePlayer inGamePlayer, int orderNumber) {
        inGamePlayer.saveOrder(orderNumber);

        TeamInfoData teamInfoData = inGamePlayer.getTeam().equals(Team.A) ? teamAInfo : teamBInfo;
        teamInfoData.getOrderList()[orderNumber - 1] = GameOrderDto.builder()
                .memberId(inGamePlayer.getMemberId())
                .nickname(inGamePlayer.getNickname())
                .image(inGamePlayer.getImage()).build();
    }

    public void addRequestCount() {
        requestCount++;
    }

    public boolean allPlayerCanStart() {
        return requestCount >= maxMemberNum;
    }

    public TeamInfoData getTeamInfoData(Team team) {
        return Team.A.equals(team) ? teamAInfo : teamBInfo;
    }

    public void completeSelect(Team team) {
        TeamInfoData teamInfoData = getTeamInfoData(team);
        teamInfoData.completeSelect();
    }

    public boolean isAllSelected() {
        return teamAInfo.isSelected() && teamBInfo.isSelected();
    }
}