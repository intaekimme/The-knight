package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.player.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InGame {
    private GameStatus gameStatus;
    private Team currentAttackTeam;
    private TeamInfoData teamAInfo;
    private TeamInfoData teamBInfo;
    private int maxMemberNum;
    private TurnData turnData;
    private int requestCount;
    private int doubtPassCount;

    @Builder
    public InGame(GameStatus gameStatus, Team currentAttackTeam, TeamInfoData teamAInfo, TeamInfoData teamBInfo, int maxMemberNum, TurnData turnData) {
        this.gameStatus = gameStatus;
        this.currentAttackTeam = currentAttackTeam;
        this.teamAInfo = teamAInfo;
        this.teamBInfo = teamBInfo;
        this.maxMemberNum = maxMemberNum;
        this.turnData = turnData;
    }

    public void initTurnData() {
        turnData = new TurnData();
    }

    public void choiceOrder(InGamePlayer inGamePlayer, int orderNumber, TeamInfoData teamInfoData) {
        GameOrderDto playerOrderInfo = null;
        int preOrderNumber = inGamePlayer.getOrder();
        if (preOrderNumber != 0) {
            playerOrderInfo = teamInfoData.getOrderList()[preOrderNumber - 1];
            teamInfoData.getOrderList()[preOrderNumber - 1] = null;
        }
        inGamePlayer.saveOrder(orderNumber);

        if (playerOrderInfo == null) {
            playerOrderInfo = GameOrderDto.builder()
                    .memberId(inGamePlayer.getMemberId())
                    .nickname(inGamePlayer.getNickname())
                    .image(inGamePlayer.getImage()).build();
        }
        teamInfoData.getOrderList()[orderNumber - 1] = playerOrderInfo;
    }

    public void addRequestCount() {
        requestCount++;
    }

    public void addDoubtPassCount() { doubtPassCount++; }

    public boolean isFullCount() {
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

    public void changeStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public boolean isTurnDataEmpty() { return this.turnData == null; }

    public void recordTurnData(TurnData turnData){
        this.turnData = turnData;
    }

    public void initRequestCount() {
        requestCount = 0;
    }

    public void initDoubtPassCount() { doubtPassCount = 0; }

    public boolean getLyingData() {
        return (gameStatus.equals(GameStatus.ATTACK_DOUBT) && turnData.isLyingAttack())
                || (gameStatus.equals(GameStatus.DEFENSE_DOUBT) && turnData.isLyingDefend());
    }

    public void setDoubtData(DoubtData doubtData) {
        turnData.setDoubtData(doubtData);
    }

    public void clearDoubtData() {
        turnData.setDoubtData(null);
    }

    public Team updateCurrentAttackTeam(){
        this.currentAttackTeam = this.currentAttackTeam == Team.A ? Team.B : Team.A;
        return this.currentAttackTeam;
    }

}
