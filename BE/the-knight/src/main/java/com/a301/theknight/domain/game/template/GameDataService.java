package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;

public interface GameDataService {
    void makeData(long gameId);

    void sendScreenData(long gameId, SendMessageService messageService);
}
