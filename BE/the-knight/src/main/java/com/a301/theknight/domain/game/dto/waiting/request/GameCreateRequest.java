package com.a301.theknight.domain.game.dto.waiting.request;

import com.a301.theknight.domain.game.entity.Game;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameCreateRequest {
    @NotBlank(message = "The game title must have a value.")
    @Size(min=1, max=100, message = "Title(${validatedValue}) can only be between {min} and {max}.")
    private String title;

    @Min(value = 4, message = "The number of people must be greater than {value}.")
    @Max(value = 10, message = "The number of people must be less than {value}.")
    private int maxMember;

    @Min(value = 0, message = "The number of sword items must be greater than {value}.")
    @Max(value = 10, message = "The number of sword items must be less than {value}.")
    private int sword;

    @Min(value = 0, message = "The number of twin items must be greater than {value}.")
    @Max(value = 10, message = "The number of twin items must be less than {value}.")
    private int twin;

    @Min(value = 0, message = "The number of shield items must be greater than {value}.")
    @Max(value = 10, message = "The number of shield items must be less than {value}.")
    private int shield;

    @Min(value = 0, message = "The number of hand items must be greater than {value}.")
    @Max(value = 10, message = "The number of hand items must be less than {value}.")
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
