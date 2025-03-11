package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {

    private final String nickname;
    private final String bearerToken;

    public SignupResponse(String nickname, String bearerToken) {
        this.nickname = nickname;
        this.bearerToken = bearerToken;
    }
}
