package com.mallangs.domain.image.dto;

import com.mallangs.domain.image.entity.Image;
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
        private Long imageId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DetailResponse {
        private Long imageId;
        private Integer width;
        private Integer height;

        public static DetailResponse from(Image image) {
            return DetailResponse.builder()
                    .imageId(image.getImageId())
                    .width(image.getWidth())
                    .height(image.getHeight())
                    .build();
        }
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