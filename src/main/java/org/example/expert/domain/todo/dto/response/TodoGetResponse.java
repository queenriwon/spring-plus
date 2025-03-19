package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

@Getter
public class TodoGetResponse {

    private final String title;
    private final Long managerCount;
    private final Long commentCount;

    public TodoGetResponse(String title, Long managerCount, Long commentCount) {
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }

}
