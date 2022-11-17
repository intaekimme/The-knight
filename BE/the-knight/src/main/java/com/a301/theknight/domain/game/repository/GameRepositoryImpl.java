package com.a301.theknight.domain.game.repository;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.a301.theknight.domain.game.entity.QGame.game;

public class GameRepositoryImpl implements GameCustomRepository {

    private final JPAQueryFactory queryFactory;

    public GameRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Game> findGameList(String keyword, Pageable pageable) {
        JPAQuery<Game> query = queryFactory.selectFrom(game)
                .where(keywordLike(keyword), notInEndStatus())
                .orderBy(game.status.desc(), game.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Game> contents = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(game.count())
                .from(game)
                .where(keywordLike(keyword), notInEndStatus());

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private BooleanExpression notInEndStatus() {
        return game.status.notIn(GameStatus.END);
    }

    private BooleanExpression keywordLike(String keyword) {
        if (!StringUtils.hasText(keyword))
            return null;
        String trimKeyword = keyword.replace(" ", "");

        StringExpression se1 = Expressions.stringTemplate("replace({0}, ' ', '')", game.title);
        return se1.containsIgnoreCase(trimKeyword);
    }
}
