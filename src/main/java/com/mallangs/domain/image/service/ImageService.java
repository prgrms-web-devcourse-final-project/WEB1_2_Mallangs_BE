package com.mallangs.domain.image.service;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.repository.ArticleRepository;
import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.repository.BoardRepository;
import com.mallangs.domain.image.dto.ImageDto;
import com.mallangs.domain.image.entity.Image;
import com.mallangs.domain.image.repository.ImageRepository;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.pet.repository.PetRepository;
import com.mallangs.domain.review.entity.Review;
import com.mallangs.domain.review.repository.ReviewRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import com.mallangs.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final ImageRepository imageRepository;
    private final ArticleRepository articleRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    private static final int MAX_IMAGE_COUNT = 4;
    private static final int MAX_FILE_SIZE = 3 * 1024 * 1024; // 3MB

    // Article 이미지 처리
    public List<ImageDto.SimpleResponse> uploadArticleImages(Long articleId, List<MultipartFile> files) {
        validateImageUpload(files);
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));
        List<Image> images = uploadImages(files, ImageTarget.builder().article(article).build());
        return images.stream()
                .map(this::toSimpleResponse)
                .collect(Collectors.toList());
    }

    public List<ImageDto.DetailResponse> getArticleImages(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));
        return imageRepository.findByArticle(article).stream()
                .map(this::toDetailResponse)
                .collect(Collectors.toList());
    }

    public void deleteArticleImages(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));
        List<Image> images = imageRepository.findByArticle(article);
        deleteImages(images);
        imageRepository.deleteByArticle(article);
    }

    // Board 이미지 처리
    public List<ImageDto.SimpleResponse> uploadBoardImages(Long boardId, List<MultipartFile> files) {
        validateImageUpload(files);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.BOARD_NOT_FOUND));
        List<Image> images = uploadImages(files, ImageTarget.builder().board(board).build());
        return images.stream()
                .map(this::toSimpleResponse)
                .collect(Collectors.toList());
    }

    public List<ImageDto.DetailResponse> getBoardImages(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.BOARD_NOT_FOUND));
        return imageRepository.findByBoard(board).stream()
                .map(this::toDetailResponse)
                .collect(Collectors.toList());
    }

    public void deleteBoardImages(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.BOARD_NOT_FOUND));
        List<Image> images = imageRepository.findByBoard(board);
        deleteImages(images);
        imageRepository.deleteByBoard(board);
    }

    // Member 이미지 처리
    public List<ImageDto.SimpleResponse> uploadMemberImages(Long memberId, List<MultipartFile> files) {
        validateImageUpload(files);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
        List<Image> images = uploadImages(files, ImageTarget.builder().member(member).build());
        return images.stream()
                .map(this::toSimpleResponse)
                .collect(Collectors.toList());
    }

    public List<ImageDto.DetailResponse> getMemberImages(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
        return imageRepository.findByMember(member).stream()
                .map(this::toDetailResponse)
                .collect(Collectors.toList());
    }

    public void deleteMemberImages(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));
        List<Image> images = imageRepository.findByMember(member);
        deleteImages(images);
        imageRepository.deleteByMember(member);
    }

    // Pet 이미지 처리
    public List<ImageDto.SimpleResponse> uploadPetImages(Long petId, List<MultipartFile> files) {
        validateImageUpload(files);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));
        List<Image> images = uploadImages(files, ImageTarget.builder().pet(pet).build());
        return images.stream()
                .map(this::toSimpleResponse)
                .collect(Collectors.toList());
    }

    public List<ImageDto.DetailResponse> getPetImages(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));
        return imageRepository.findByPet(pet).stream()
                .map(this::toDetailResponse)
                .collect(Collectors.toList());
    }

    public void deletePetImages(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.PET_NOT_FOUND));
        List<Image> images = imageRepository.findByPet(pet);
        deleteImages(images);
        imageRepository.deleteByPet(pet);
    }

    // Review 이미지 처리
    public List<ImageDto.SimpleResponse> uploadReviewImages(Long reviewId, List<MultipartFile> files) {
        validateImageUpload(files);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.REVIEW_NOT_FOUND));
        List<Image> images = uploadImages(files, ImageTarget.builder().review(review).build());
        return images.stream()
                .map(this::toSimpleResponse)
                .collect(Collectors.toList());
    }

    public List<ImageDto.DetailResponse> getReviewImages(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.REVIEW_NOT_FOUND));
        return imageRepository.findByReview(review).stream()
                .map(this::toDetailResponse)
                .collect(Collectors.toList());
    }

    public void deleteReviewImages(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.REVIEW_NOT_FOUND));
        List<Image> images = imageRepository.findByReview(review);
        deleteImages(images);
        imageRepository.deleteByReview(review);
    }

    // 이미지 타겟 엔티티
    @lombok.Builder
    private static class ImageTarget {
        private Article article;
        private Board board;
        private Member member;
        private Pet pet;
        private Review review;
    }

    // Private 헬퍼 메서드
    private List<Image> uploadImages(List<MultipartFile> files, ImageTarget target) {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            validateFileSize(file);

            try {
                BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                String storedFilename = generateStoredFilename(file.getOriginalFilename());
                String fileUrl = s3Service.uploadFile(file, storedFilename);

                Image image = Image.builder()
                        .article(target.article)
                        .board(target.board)
                        .member(target.member)
                        .pet(target.pet)
                        .review(target.review)
                        .originalFileName(file.getOriginalFilename())
                        .storedFileName(storedFilename)
                        .filePath(fileUrl)
                        .fileType(file.getContentType())
                        .fileSize((int) file.getSize())
                        .width(bufferedImage.getWidth())
                        .height(bufferedImage.getHeight())
                        .build();

                images.add(imageRepository.save(image));
            } catch (IOException e) {
                throw new MallangsCustomException(ErrorCode.IMAGE_PROCESSING_ERROR);
            }
        }
        return images;
    }

    private void deleteImages(List<Image> images) {
        for (Image image : images) {
            s3Service.deleteFile(image.getStoredFileName());
        }
    }

    private ImageDto.SimpleResponse toSimpleResponse(Image image) {
        return ImageDto.SimpleResponse.builder()
                .url(image.getFilePath())
                .build();
    }

    private ImageDto.DetailResponse toDetailResponse(Image image) {
        return ImageDto.DetailResponse.builder()
                .url(image.getFilePath())
                .width(image.getWidth())
                .height(image.getHeight())
                .originalFileName(image.getOriginalFileName())
                .fileSize(image.getFileSize())
                .build();
    }

    private void validateImageUpload(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new MallangsCustomException(ErrorCode.IMAGE_FILE_IS_REQUIRED);
        }
        if (files.size() > MAX_IMAGE_COUNT) {
            throw new MallangsCustomException(ErrorCode.IMAGE_COUNT_EXCEEDED);
        }
    }

    private void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new MallangsCustomException(ErrorCode.IMAGE_SIZE_EXCEEDED);
        }
    }

    private String generateStoredFilename(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }
}