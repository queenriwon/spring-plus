package org.example.expert.domain.log.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String traceId;
    private String url;
    private LocalDateTime requestTime;
    private String method;
    private String body;

    // 요청
    public Log(String url, String method, String body) {
        this.traceId = UUID.randomUUID().toString().substring(0, 8);
        this.url = url;
        this.requestTime = LocalDateTime.now();
        this.method = method;
        this.body = body;
    }
}
