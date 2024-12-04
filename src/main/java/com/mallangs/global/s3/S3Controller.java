package com.mallangs.global.s3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "S3 파일 관리", description = "S3 파일 업로드 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "파일 업로드", description = "S3에 이미지 파일을 업로드합니다.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = s3Service.uploadImage(file);
            log.info("File uploaded successfully. imageUrl: {}", imageUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(imageUrl);
        } catch (IOException e) {
            log.error("File upload failed. Error message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File upload failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Internal server error occurred during file upload. Error message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred: " + e.getMessage());
        }
    }
}