package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SigninResponse {

    private final String nickname;
    private final String bearerToken;

    public SigninResponse(String nickname, String bearerToken) {
        this.nickname = nickname;
        this.bearerToken = bearerToken;
    }
}
