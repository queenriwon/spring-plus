package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.request.UserProfileImageRequest;
import org.example.expert.domain.user.dto.response.UserProfileResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PatchMapping("/users/password")
    public void changePassword(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getUserId(), userChangePasswordRequest);
    }

    @PatchMapping("/users/profile-image")
    public ResponseEntity<UserProfileResponse> updateProfileImage(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UserProfileImageRequest userProfileImageRequest
    ) {
        return ResponseEntity.ok(userService.updateProfileImage(authUser, userProfileImageRequest));
    }
}
