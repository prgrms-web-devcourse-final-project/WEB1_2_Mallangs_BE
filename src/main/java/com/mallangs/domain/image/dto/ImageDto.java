package com.mallangs.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UploadRequest {
        private List<MultipartFile> files;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SimpleResponse {
        private String url;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DetailResponse {
        private String url;
        private Integer width;
        private Integer height;
        private String originalFileName;
        private Integer fileSize;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ImageListResponse {
        private List<DetailResponse> images;
        private String message;
        private int count;

        public static ImageListResponse of(List<DetailResponse> images) {
            String message = images.isEmpty() ?
                    "등록된 이미지가 없습니다." :
                    "이미지 조회 성공";

            return ImageListResponse.builder()
                    .images(images)
                    .message(message)
                    .count(images.size())
                    .build();
        }
    }
}