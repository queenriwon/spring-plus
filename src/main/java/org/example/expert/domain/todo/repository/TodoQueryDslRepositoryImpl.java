package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoGetResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;

@RequiredArgsConstructor
public class TodoQueryDslRepositoryImpl implements TodoQueryDslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(todo)
                        .leftJoin(todo.user).fetchJoin()
                        .where(todo.id.eq(todoId))
                        .fetchFirst()
        );
    }

    @Override
    public Page<TodoGetResponse> findByTitleOrCreatedAtOrManager(
            String title,
            LocalDate startAt,
            LocalDate endAt,
            String managerName,
            Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if (title != null) {
            builder.and(todo.title.contains(title));
        }

        if (startAt != null) {
            builder.and(todo.createdAt.goe(startAt.atStartOfDay()));
        }

        if (endAt != null) {
            builder.and(todo.createdAt.loe(endAt.atStartOfDay()));
        }

        if (managerName != null) {
            builder.and(manager.user.nickname.contains(managerName));
        }

        List<TodoGetResponse> todoGetResponseList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                TodoGetResponse.class,
                                todo.title,
                                manager.countDistinct(),
                                comment.countDistinct()
                        )
                )
                .from(todo)
                .leftJoin(manager).on(manager.todo.id.eq(todo.id))      // fetchJoin(): 프로젝션 x
                .leftJoin(comment).on(comment.todo.id.eq(todo.id))
                .where(builder)
                .groupBy(todo.id, todo.title)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(builder);

        return PageableExecutionUtils.getPage(todoGetResponseList, pageable, countQuery::fetchOne);
    }
}