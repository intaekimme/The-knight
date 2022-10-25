package com.a301.theknight.domain.player.repository;

import com.a301.theknight.domain.player.entity.Player;

import java.util.List;

public interface PlayerCustomRepository {
    List<Player> findTenByMemberId(long memberId);
}
