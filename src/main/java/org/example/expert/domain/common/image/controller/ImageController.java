package org.example.expert.domain.common.image.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.image.dto.ImageResponse;
import org.example.expert.domain.common.image.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<ImageResponse> upload(
            @RequestPart MultipartFile image
    ) {
        String imageUrl = s3Service.uploadImage(image);
        return ResponseEntity.ok(new ImageResponse(imageUrl));
    }
}
