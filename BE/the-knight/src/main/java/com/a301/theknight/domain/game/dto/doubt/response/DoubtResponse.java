package com.a301.theknight.domain.game.dto.doubt.response;


import com.a301.theknight.domain.game.dto.doubt.request.DoubtPlayerIdDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoubtResponse {
    private DoubtPlayerDto suspect;
    private DoubtPlayerDto suspected;
    private DoubtOpenDto open;
    private boolean keepDefense;
    private DoubtPlayerIdDto nextDefender;
    private boolean SuspiciousResult;
}
