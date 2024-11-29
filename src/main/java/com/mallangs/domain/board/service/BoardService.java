package com.mallangs.domain.board.service;

import com.mallangs.domain.board.dto.request.CommunityCreateRequest;
import com.mallangs.domain.board.dto.request.CommunityUpdateRequest;
import com.mallangs.domain.board.dto.request.SightingCreateRequest;
import com.mallangs.domain.board.dto.request.SightingUpdateRequest;
import com.mallangs.domain.board.dto.response.*;
import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.entity.BoardStatus;
import com.mallangs.domain.board.entity.BoardType;
import com.mallangs.domain.board.entity.Category;
import com.mallangs.domain.board.repository.BoardRepository;
import com.mallangs.domain.board.repository.CategoryRepository;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    
    // 커뮤니티 게시글 작성
    @Transactional
    public Long createCommunityBoard(CommunityCreateRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Board board = Board.createCommunityBoard(
                member,
                category,
                request.getTitle(),
                request.getContent(),
                request.getContent()
        );

        return boardRepository.save(board).getBoardId();
    }

    // 실종신고 - 목격제보 게시글 작성
    @Transactional
    public Long createSightingBoard(SightingCreateRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Board board = Board.createSightingBoard(
                member,
                category,
                request.getTitle(),
                request.getContent(),
                request.getLatitude(),
                request.getLongitude(),
                request.getAddress(),
                request.getSightedAt(),
                request.getImgUrl()
        );

        return boardRepository.save(board).getBoardId();
    }

    // 커뮤니티 게시글 수정
    @Transactional
    public void updateCommunityBoard(Long boardId, CommunityUpdateRequest request, Long memberId) {
        Board board = getBoardWithMemberValidation(boardId, memberId, BoardType.COMMUNITY);

        board.change(
                request.getTitle(),
                request.getContent(),
                null,
                null,
                null,
                null,
                request.getImgUrl()
        );
    }

    // 실종신고 - 목격제보 게시글 수정
    @Transactional
    public void updateSightingBoard(Long boardId, SightingUpdateRequest request, Long memberId) {
        Board board = getBoardWithMemberValidation(boardId, memberId, BoardType.SIGHTING);

        board.change(
                request.getTitle(),
                request.getContent(),
                request.getLatitude(),
                request.getLongitude(),
                request.getAddress(),
                request.getSightedAt(),
                request.getImgUrl()
        );
    }

    // 커뮤니티 게시글 상세 조회
    public CommunityDetailResponse getCommunityBoard(Long boardId) {
        Board board = getBoardWithTypeValidation(boardId, BoardType.COMMUNITY);
        board.increaseViewCount();
        return new CommunityDetailResponse(board);
    }

    // 실종신고 - 목격제보 게시글 상세 조회
    public SightingDetailResponse getSightingBoard(Long boardId) {
        Board board = getBoardWithTypeValidation(boardId, BoardType.SIGHTING);
        board.increaseViewCount();
        return new SightingDetailResponse(board);
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardId, Long memberId, BoardType boardType) {
        Board board = getBoardWithMemberValidation(boardId, memberId, boardType);
        board.changeStatus(BoardStatus.HIDDEN);
    }

    // 카테고리별 커뮤니티 게시글 목록 조회
    public Page<CommunityListResponse> getCommunityBoardsByCategory(Long categoryId, Pageable pageable) {
        return boardRepository.findByCategoryId(categoryId, BoardType.COMMUNITY, pageable)
                .map(CommunityListResponse::new);
    }

    // 카테고리별 목격 게시글 목록 조회
    public Page<SightingListResponse> getSightingBoardsByCategory(Long categoryId, Pageable pageable) {
        return boardRepository.findByCategoryId(categoryId, BoardType.SIGHTING, pageable)
                .map(SightingListResponse::new);
    }

    // 키워드로 커뮤니티 게시글 검색
    public Page<CommunityListResponse> searchCommunityBoards(String keyword, Pageable pageable) {
        return boardRepository.searchByTitleOrContent(keyword, pageable)
                .map(CommunityListResponse::new);
    }

    // 키워드로 목격 게시글 검색
    public Page<SightingListResponse> searchSightingBoards(String keyword, Pageable pageable) {
        return boardRepository.searchByTitleOrContent(keyword, pageable)
                .map(SightingListResponse::new);
    }

    // 특정 회원의 커뮤니티 게시글 목록 조회
    public Page<CommunityListResponse> getMemberCommunityBoards(Long memberId, Pageable pageable) {
        return boardRepository.findByMemberId(memberId, pageable)
                .map(CommunityListResponse::new);
    }

    // 특정 회원의 목격 게시글 목록 조회
    public Page<SightingListResponse> getMemberSightingBoards(Long memberId, Pageable pageable) {
        return boardRepository.findByMemberId(memberId, pageable)
                .map(SightingListResponse::new);
    }

    // 관리자용 - 상태별 게시글 조회
    public Page<AdminBoardResponse> getBoardsByStatus(BoardStatus status, Pageable pageable) {
        return boardRepository.findByStatus(status, pageable)
                .map(AdminBoardResponse::from);
    }

    // 관리자용 - 카테고리와 제목으로 게시글 검색
    public Page<AdminBoardResponse> searchBoardsForAdmin(Long categoryId, String keyword, Pageable pageable) {
        return boardRepository.searchForAdmin(categoryId, keyword, pageable)
                .map(AdminBoardResponse::from);
    }

    // 관리자용 - 카테고리, 상태, 제목으로 게시글 검색
    public Page<AdminBoardResponse> searchBoardsForAdminWithStatus(
            Long categoryId, BoardStatus status, String keyword, Pageable pageable) {
        return boardRepository.searchForAdminWithStatus(categoryId, status, keyword, pageable)
                .map(AdminBoardResponse::from);
    }

    // 관리자용 - 게시글 상태 변경 (다중 선택 가능)
    @Transactional
    public void changeBoardStatus(List<Long> boardIds, BoardStatus status) {
        List<Board> boards = boardRepository.findAllById(boardIds);
        boards.forEach(board -> board.changeStatus(status));
    }

    // 게시글 작성자 및 타입 검증
    private Board getBoardWithMemberValidation(Long boardId, Long memberId, BoardType boardType) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.BOARD_NOT_FOUND));

        if (!board.getMember().getMemberId().equals(memberId)) {
            throw new MallangsCustomException(ErrorCode.UNAUTHORIZED_BOARD_ACCESS);
        }

        if (board.getBoardType() != boardType) {
            throw new MallangsCustomException(ErrorCode.INVALID_BOARD_TYPE);
        }

        return board;
    }

    // 게시글 타입 검증
    private Board getBoardWithTypeValidation(Long boardId, BoardType boardType) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.BOARD_NOT_FOUND));

        if (board.getBoardStatus() != BoardStatus.PUBLISHED) {
            throw new MallangsCustomException(ErrorCode.INVALID_BOARD_STATUS);
        }

        if (board.getBoardType() != boardType) {
            throw new MallangsCustomException(ErrorCode.INVALID_BOARD_TYPE);
        }

        return board;
    }
}