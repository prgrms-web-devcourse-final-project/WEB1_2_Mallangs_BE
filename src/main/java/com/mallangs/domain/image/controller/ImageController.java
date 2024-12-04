package com.mallangs.domain.image.controller;

import com.mallangs.domain.image.dto.ImageDto;
import com.mallangs.domain.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "이미지 API", description = "게시판 별 이미지 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    // Article 이미지 API
    @Operation(summary = "Article 이미지 저장", description = "글타래에 이미지를 저장합니다.")
    @PostMapping(value = "/articles/{articleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageDto.SimpleResponse>> uploadArticleImages(
            @PathVariable Long articleId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(imageService.uploadArticleImages(articleId, files));
    }

    @Operation(summary = "Article 이미지 조회", description = "글타래에 저장된 이미지의 정보를 조회합니다.")
    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ImageDto.ImageListResponse> getArticleImages(@PathVariable Long articleId) {
        List<ImageDto.DetailResponse> images = imageService.getArticleImages(articleId);
        return ResponseEntity.ok(ImageDto.ImageListResponse.of(images));
    }

    @Operation(summary = "Article 이미지 삭제", description = "글타래에 저장된 이미지를 삭제합니다.")
    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<Void> deleteArticleImages(@PathVariable Long articleId) {
        imageService.deleteArticleImages(articleId);
        return ResponseEntity.noContent().build();
    }

    // Board 이미지 API
    @Operation(summary = "Board 이미지 저장", description = "게시판에 이미지를 저장합니다.")
    @PostMapping(value = "/boards/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageDto.SimpleResponse>> uploadBoardImages(
            @PathVariable Long boardId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(imageService.uploadBoardImages(boardId, files));
    }

    @Operation(summary = "Board 이미지 조회", description = "게시판에 저장된 이미지의 정보를 조회합니다.")
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<ImageDto.ImageListResponse> getBoardImages(@PathVariable Long boardId) {
        List<ImageDto.DetailResponse> images = imageService.getBoardImages(boardId);
        return ResponseEntity.ok(ImageDto.ImageListResponse.of(images));
    }

    @Operation(summary = "Board 이미지 삭제", description = "게시판에 저장된 이미지를 삭제합니다.")
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<Void> deleteBoardImages(@PathVariable Long boardId) {
        imageService.deleteBoardImages(boardId);
        return ResponseEntity.noContent().build();
    }

    // Member 이미지 API
    @Operation(summary = "Member 이미지 저장", description = "회원에 이미지를 저장합니다.")
    @PostMapping(value = "/members/{memberId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageDto.SimpleResponse>> uploadMemberImages(
            @PathVariable Long memberId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(imageService.uploadMemberImages(memberId, files));
    }

    @Operation(summary = "Member 이미지 조회", description = "회원에 저장된 이미지의 정보를 조회합니다.")
    @GetMapping("/members/{memberId}")
    public ResponseEntity<ImageDto.ImageListResponse> getMemberImages(@PathVariable Long memberId) {
        List<ImageDto.DetailResponse> images = imageService.getMemberImages(memberId);
        return ResponseEntity.ok(ImageDto.ImageListResponse.of(images));
    }

    @Operation(summary = "Member 이미지 삭제", description = "회원에 저장된 이미지를 삭제합니다.")
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> deleteMemberImages(@PathVariable Long memberId) {
        imageService.deleteMemberImages(memberId);
        return ResponseEntity.noContent().build();
    }

    // Pet 이미지 API
    @Operation(summary = "Pet 이미지 저장", description = "반려동물(말랑이)에 이미지를 저장합니다.")
    @PostMapping(value = "/pets/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageDto.SimpleResponse>> uploadPetImages(
            @PathVariable Long petId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(imageService.uploadPetImages(petId, files));
    }

    @Operation(summary = "Pet 이미지 조회", description = "반려동물(말랑이)에 저장된 이미지의 정보를 조회합니다.")
    @GetMapping("/pets/{petId}")
    public ResponseEntity<ImageDto.ImageListResponse> getPetImages(@PathVariable Long petId) {
        List<ImageDto.DetailResponse> images = imageService.getPetImages(petId);
        return ResponseEntity.ok(ImageDto.ImageListResponse.of(images));
    }

    @Operation(summary = "Pet 이미지 삭제", description = "반려동물(말랑이)에 저장된 이미지를 삭제합니다.")
    @DeleteMapping("/pets/{petId}")
    public ResponseEntity<Void> deletePetImages(@PathVariable Long petId) {
        imageService.deletePetImages(petId);
        return ResponseEntity.noContent().build();
    }

    // Review 이미지 API
    @Operation(summary = "Review 이미지 저장", description = "리뷰에 이미지를 저장합니다.")
    @PostMapping(value = "/reviews/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageDto.SimpleResponse>> uploadReviewImages(
            @PathVariable Long reviewId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(imageService.uploadReviewImages(reviewId, files));
    }

    @Operation(summary = "Review 이미지 조회", description = "리븊에 저장된 이미지의 정보를 조회합니다.")
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ImageDto.ImageListResponse> getReviewImages(@PathVariable Long reviewId) {
        List<ImageDto.DetailResponse> images = imageService.getReviewImages(reviewId);
        return ResponseEntity.ok(ImageDto.ImageListResponse.of(images));
    }

    @Operation(summary = "Review 이미지 삭제", description = "리뷰에 저장된 이미지를 삭제합니다.")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReviewImages(@PathVariable Long reviewId) {
        imageService.deleteReviewImages(reviewId);
        return ResponseEntity.noContent().build();
    }

    // ChatMessage 이미지 API
    @Operation(summary = "ChatMessage 이미지 저장", description = "채팅에 이미지를 저장합니다.")
    @PostMapping(value = "/chat/{chatMessageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageDto.SimpleResponse>> uploadChatMessage(
            @PathVariable Long chatMessageId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(imageService.uploadChatMessageImages(chatMessageId, files));
    }

    @Operation(summary = "Review 이미지 조회", description = "리븊에 저장된 이미지의 정보를 조회합니다.")
    @GetMapping("/chat/{chatMessageId}")
    public ResponseEntity<ImageDto.ImageListResponse> getChatMessageImages(@PathVariable Long chatMessageId) {
        List<ImageDto.DetailResponse> images = imageService.getChatMessageImages(chatMessageId);
        return ResponseEntity.ok(ImageDto.ImageListResponse.of(images));
    }

    @Operation(summary = "Review 이미지 삭제", description = "리뷰에 저장된 이미지를 삭제합니다.")
    @DeleteMapping("/chat/{chatMessageId}")
    public ResponseEntity<Void> deleteChatMessageImages(@PathVariable Long chatMessageId) {
        imageService.deleteChatMessageImages(chatMessageId);
        return ResponseEntity.noContent().build();
    }


}