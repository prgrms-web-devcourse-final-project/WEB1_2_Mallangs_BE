package com.mallangs.domain.community.repository;

import com.mallangs.domain.community.entity.Community;
import com.mallangs.domain.community.entity.CommunityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    // 카테고리별 게시글 목록 조회 (최신순으로 정렬)
    @Query("""
            SELECT c FROM Community c WHERE c.category.categoryId = :categoryId AND c.communityStatus = 'PUBLISHED' ORDER BY c.createdAt DESC
            """)
    Page<Community> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    // 키워드로 통합 검색 (제목 + 내용)
    @Query("""
            SELECT c FROM Community c WHERE (c.title LIKE %:keyword% OR c.content LIKE %:keyword% AND c.communityStatus = 'PUBLISHED') ORDER BY c.createdAt DESC
            """)
    Page<Community> searchByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);

    // 특정 회원이 작성한 게시글 목록 조회
    @Query("""
            SELECT c FROM Community c WHERE c.member.memberId = :memberId AND c.communityStatus = 'PUBLISHED' ORDER BY c.createdAt DESC
            """)
    Page<Community> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    // 관리자용 - 상태별 게시글 조회
    @Query("""
            SELECT c FROM Community c WHERE c.communityStatus = :status ORDER BY c.createdAt DESC
            """)
    Page<Community> findByStatus(@Param("status")CommunityStatus status, Pageable pageable);

    // 관리자용 - 카테고리와 제목으로 게시글 검색
    @Query("""
            SELECT c FROM Community c WHERE c.category.categoryId = :categoryId AND c.title LIKE %:keyword% ORDER BY c.createdAt DESC
            """)
    Page<Community> searchForAdmin(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);
}
