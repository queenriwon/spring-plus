package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoGetResponse;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    public Page<TodoResponse> getTodos(String weather, LocalDate startAt, LocalDate endAt, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        if (endAt == null) {    // endAt은 무조건 존재함
            endAt = LocalDate.now();
        }

        Page<Todo> todos;

        if (weather == null && startAt == null) {       // 전체 검색
            todos = todoRepository.findAllByOrderByModifiedAtDesc(endAt, pageable);
        } else if (weather != null && startAt == null) {       // 날씨 조건 혹은 endAt으로 조회
            todos = todoRepository.findAllByWeather(weather, endAt, pageable);
        } else if (weather == null) {       // 날짜 조건으로 검색
            todos = todoRepository.findAllByDate(startAt, endAt, pageable);
        } else {       // 날짜와 날씨 조건으로 검색
            todos = todoRepository.findAllByWeatherAndDate(weather, startAt, endAt, pageable);
        }

        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

    public Page<TodoGetResponse> searchTodos(String title, LocalDate startAt, LocalDate endAt, String managerName, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return todoRepository.findByTitleOrCreatedAtOrManager(title, startAt, endAt, managerName, pageable);
    }
}
