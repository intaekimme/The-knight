package com.a301.theknight.domain.game.dto.doubt.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoubtResponse {
    private DoubtPlayerDto suspect;
    private SuspectedPlayerDto suspected;
    private String doubtTeam;
    private boolean doubtSuccess;
}
