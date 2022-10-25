package com.a301.theknight.domain.player.repository;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.player.entity.Player;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.a301.theknight.domain.player.entity.QPlayer.player;

public class PlayerRepositoryImpl implements PlayerCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PlayerRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Player> findTenByMemberId(long memberId) {
        return queryFactory.selectFrom(player)
                .where(memberEqual(memberId), resultNotNull(), endedGame())
                .orderBy(player.updatedAt.desc())
                .limit(10).fetch();
    }

    private BooleanExpression endedGame() {
        return player.game.status.eq(GameStatus.END);
    }

    private BooleanExpression memberEqual(long memberId) {
        return player.member.id.eq(memberId);
    }

    private BooleanExpression resultNotNull() {
        return player.result.isNotNull();
    }
}
