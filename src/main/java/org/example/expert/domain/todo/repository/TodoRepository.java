package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoQueryDslRepository{

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE FUNCTION('DATE', t.modifiedAt) <= :endAt " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(
            @Param("endAt") LocalDate endAt,
            Pageable pageable
    );

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE FUNCTION('DATE', t.modifiedAt) <= :endAt " +
            "AND t.weather = :weather " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByWeather(
            @Param("weather") String weather,
            @Param("endAt") LocalDate endAt,
            Pageable pageable
    );

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE FUNCTION('DATE', t.modifiedAt) BETWEEN :startAt AND :endAt " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByDate(
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt,
            Pageable pageable
    );

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE FUNCTION('DATE', t.modifiedAt) BETWEEN :startAt AND :endAt " +
            "AND t.weather = :weather " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByWeatherAndDate(
            @Param("weather") String weather,
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt,
            Pageable pageable
    );
}
