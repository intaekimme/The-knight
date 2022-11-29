package com.a301.theknight.domain.game.dto.waiting.response;

import com.a301.theknight.domain.game.entity.Game;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameInfoResponse {
    private long gameId;
    private String title;
    private int maxMember;
    private int currentMembers;
    private int sword;
    private int twin;
    private int shield;
    private int hand;
    private long ownerId;

    public static GameInfoResponse toDto(Game game) {
        return GameInfoResponse.builder()
                .gameId(game.getId())
                .title(game.getTitle())
                .maxMember(game.getCapacity())
                .currentMembers(game.getPlayers().size())
                .sword(game.getSword())
                .twin(game.getTwin())
                .shield(game.getShield())
                .hand(game.getHand())
                .ownerId(game.getOwner().getMember().getId())
                .build();
    }
}
