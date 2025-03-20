package org.example.expert.domain.common.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ImageUploadService implements ImageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucketName}")
    private String bucket;

    /**
     * S3에 이미지 업로드
     */
    public String uploadImage(MultipartFile image) {
        validateFile(image);

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(image.getContentType())
                    .contentLength(image.getSize())
                    .build();

            // S3 업로드
            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(image.getBytes())
            );

            if (response.sdkHttpResponse().isSuccessful()) {
                return fileName;
            } else {
                throw new RuntimeException("S3 업로드 실패: " + response.sdkHttpResponse().statusText().orElse("Unknown error"));
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }

    /**
     * 파일 검증: 허용된 파일 형식 및 크기 체크
     */
    private void validateFile(MultipartFile image) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        // 파일 확장자 검사 (MIME 타입 외에도 확장자로 검사)
        String fileExtension = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
        if (!Set.of("jpg", "jpeg", "png").contains(fileExtension)) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. (jpg, jpeg, png만 가능)");
        }

        if (image.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기가 5MB를 초과할 수 없습니다.");
        }
    }
}