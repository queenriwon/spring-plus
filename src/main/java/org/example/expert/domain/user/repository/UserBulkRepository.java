package org.example.expert.domain.user.repository;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserBulkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final int BATCH_SIZE = 1000;

    public void bulkInsertUsers(List<User> users) {
        String sql = "INSERT INTO users (email,password,nickname) values (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, users, BATCH_SIZE, (ps, argument) -> {
            ps.setString(1, argument.getEmail());
            ps.setString(2, argument.getPassword());
            ps.setString(3, argument.getNickname());
        });

    }
}
