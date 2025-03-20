package org.example.expert.domain.common.image.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.image.dto.ImageResponse;
import org.example.expert.domain.common.image.service.S3ImageUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final S3ImageUploadService imageService;

    @PostMapping
    public ResponseEntity<ImageResponse> upload(
            @ModelAttribute MultipartFile image
    ) {
        String imageUrl = imageService.uploadImage(image);
        return ResponseEntity.ok(new ImageResponse(imageUrl));
    }
}
