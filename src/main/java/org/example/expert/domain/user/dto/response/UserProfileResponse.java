package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserProfileResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String profileUrl;

    public UserProfileResponse(Long id, String email, String nickname, String profileUrl) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }
}
