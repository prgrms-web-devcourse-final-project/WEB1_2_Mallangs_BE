package com.mallangs.domain.comment.repository;

import com.mallangs.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Board 게시물 조회
    @Query("SELECT c FROM Comment c WHERE c.board.boardId = :boardId")
    Page<Comment> findCommentsByBoardId(@Param("boardId") Long boardId, Pageable pageable);

    // Article 게시물 조회
    @Query("SELECT c FROM Comment c WHERE c.article.id = :articleId")
    Page<Comment> findCommentsArticleId(@Param("articleId") Long articleId, Pageable pageable);

    // 회원 별 댓글 목록 조회
    @Query("SELECT c FROM Comment c WHERE c.member.memberId = :memberId")
    Page<Comment> findCommentsByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    // 닉네임 별 댓글 목록 조회
    @Query("SELECT c FROM Comment c WHERE c.member.nickname.value LIKE %:nickname%")
    Page<Comment> findCommentsByNickname(@Param("nickname") String nickname, Pageable pageable);

    // 댓글 내용 별 댓글 목록 조회
    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:content%")
    Page<Comment> findCommentsByContent(@Param("content") String content, Pageable pageable);

    // 댓글 생성 범위 별 댓글 목록 조회
    @Query("SELECT c FROM Comment c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    Page<Comment> findCommentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
