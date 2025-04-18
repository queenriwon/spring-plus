package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoGetResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface TodoQueryDslRepository {

    Optional<Todo> findByIdWithUser(Long todoId);

    Page<TodoGetResponse> findByTitleOrCreatedAtOrManager(String title, LocalDate startAt, LocalDate endAt, String managerName, Pageable pageable);
}
