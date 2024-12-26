package com.mallangs.domain.board.repository;

import com.mallangs.domain.board.dto.response.BoardStatusCount;
import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.entity.BoardStatus;
import com.mallangs.domain.board.entity.BoardType;
import com.mallangs.domain.member.entity.embadded.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 게시판 타입별 전체 게시글 조회
    @Query("""
            SELECT b FROM Board b WHERE b.boardStatus = 'PUBLISHED' AND b.boardType = :boardType ORDER BY b.createdAt DESC
            """)
    Page<Board> findAllByBoardType(@Param("boardType") BoardType boardType, Pageable pageable);

    //목격게시글 List로 조회
    List<Board> findAllByBoardType(@Param("boardType") BoardType boardType);

    // 카테고리 이름으로 게시글 조회
    @Query("""
            SELECT b FROM Board b WHERE b.category.name = :name AND b.boardStatus = 'PUBLISHED' AND b.boardType = :boardType ORDER BY b.createdAt DESC
            """)
    Page<Board> findByCategoryName(@Param("name") String name, BoardType boardType, Pageable pageable);

    // 키워드로 통합 검색 (제목 + 내용)
    @Query("""
            SELECT b FROM Board b WHERE (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) AND b.boardStatus = 'PUBLISHED' AND b.boardType = :boardType ORDER BY b.createdAt DESC
            """)
    Page<Board> searchByTitleOrContent(@Param("keyword") String keyword, @Param("boardType") BoardType boardType, Pageable pageable);

    // 특정 회원이 작성한 게시글 회원 ID로 목록 조회
    @Query("""
            SELECT b FROM Board b WHERE b.member.userId = :userId AND b.boardStatus = 'PUBLISHED' AND b.boardType = :boardType ORDER BY b.createdAt DESC
            """)
    Page<Board> findByUserId(@Param("userId") UserId userId, @Param("boardType") BoardType boardType, Pageable pageable);

    // 관리자용 - 상태별 게시글 조회
    @Query("""
            SELECT b FROM Board b WHERE (:boardStatus IS NULL OR b.boardStatus = :boardStatus) ORDER BY b.createdAt DESC
            """)
    Page<Board> findByStatus(@Param("boardStatus") BoardStatus boardStatus, Pageable pageable);

    @Query("""
            SELECT new com.mallangs.domain.board.dto.response.BoardStatusCount(
                COUNT(b),
                COUNT(CASE WHEN b.boardStatus = 'PUBLIC' THEN 1 END),
                COUNT(CASE WHEN b.boardStatus = 'HIDDEN' THEN 1 END),
                COUNT(CASE WHEN b.boardStatus = 'DRAFT' THEN 1 END)
            )
            FROM Board b
            """)
    BoardStatusCount countByStatus();

    // 관리자용 - 카테고리와 제목으로 게시글 검색
    @Query("""
            SELECT b FROM Board b WHERE b.category.categoryId = :categoryId AND b.boardType = :boardType AND b.title LIKE %:keyword% ORDER BY b.createdAt DESC
            """)
    Page<Board> searchForAdmin(@Param("categoryId") Long categoryId, @Param("boardType") BoardType boardType,
                               @Param("keyword") String keyword, Pageable pageable);

    // 관리자용 - 카테고리, 상태, 제목으로 게시글 검색
    @Query("""
            SELECT b FROM Board b WHERE b.category.categoryId = :categoryId AND b.boardType = :boardType AND b.boardStatus = :boardStatus AND b.title LIKE %:keyword% ORDER BY b.createdAt DESC
            """)
    Page<Board> searchForAdminWithStatus(@Param("categoryId") Long categoryId, @Param("boardType") BoardType boardType, @Param("boardStatus") BoardStatus boardStatus, @Param("keyword") String keyword, Pageable pageable);
}
