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
    List<Image> findByArticle(Article article);
    List<Image> findByBoard(Board board);
    List<Image> findByMember(Member member);
    List<Image> findByPet(Pet pet);
    List<Image> findByReview(Review review);

    void deleteByArticle(Article article);
    void deleteByBoard(Board board);
    void deleteByMember(Member member);
    void deleteByPet(Pet pet);
    void deleteByReview(Review review);
}