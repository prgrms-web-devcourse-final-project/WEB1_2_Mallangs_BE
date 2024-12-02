package com.mallangs.domain.image.repository;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.image.entity.Image;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.pet.entity.Pet;
import com.mallangs.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    // Article에 연결된 이미지 조회
    List<Image> findByArticle(Article article);

    // Board에 연결된 이미지 조회
    List<Image> findByBoard(Board board);

    // Member에 연결된 이미지 조회
    List<Image> findByMember(Member member);

    // Pet에 연결된 이미지 조회
    List<Image> findByPet(Pet pet);

    // Review에 연결된 이미지 조회
    List<Image> findByReview(Review review);

    // Article에 연결된 이미지 삭제
    void deleteByArticle(Article article);

    // Board에 연결된 이미지 삭제
    void deleteByBoard(Board board);

    // Member에 연결된 이미지 삭제
    void deleteByMember(Member member);

    // Pet에 연결된 이미지 삭제
    void deleteByPet(Pet pet);

    // Review에 연결된 이미지 삭제
    void deleteByReview(Review review);
}
