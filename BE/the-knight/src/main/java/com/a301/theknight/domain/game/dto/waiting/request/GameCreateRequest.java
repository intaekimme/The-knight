package com.a301.theknight.domain.game.dto.waiting.request;

import com.a301.theknight.domain.game.entity.Game;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameCreateRequest {
    @NotBlank(message = "The game title must have a value.")
    @Size(min=1, max=100, message = "Title(${validatedValue}) can only be between {min} and {max}.")
    private String title;

    @Size(min=4, max=10, message = "The number of people(${validatedValue}) that can be set is between {min} and {max}.")
    private int maxMember;

    @Size(min=0, max=10, message = "The number of sword items(${validatedValue}) that can be set is between {min} and {max}.")
    private int sword;

    @Size(min=0, max=10, message = "The number of twin items(${validatedValue}) that can be set is between {min} and {max}.")
    private int twin;

    @Size(min=0, max=10, message = "The number of shield items(${validatedValue}) that can be set is between {min} and {max}.")
    private int shield;

    @Size(min=0, max=10, message = "The number of hand items(${validatedValue}) that can be set is between {min} and {max}.")
    private int hand;

    public Game toEntity(){
        return Game.builder()
                .title(title)
                .sword(sword)
                .twin(twin)
                .shield(shield)
                .hand(hand)
                .capacity(maxMember)
                .build();
    }

    @Builder
    public GameCreateRequest(String title, int maxMember, int sword, int twin, int shield, int hand){
        this.title = title;
        this.maxMember = maxMember;
        this.sword = sword;
        this.twin = twin;
        this.shield = shield;
        this.hand = hand;
    }
}
