package com.mallangs.domain.comment.service;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.repository.ArticleRepository;
import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.repository.BoardRepository;
import com.mallangs.domain.comment.dto.request.CommentArticleRequest;
import com.mallangs.domain.comment.dto.request.CommentBoardRequest;
import com.mallangs.domain.comment.dto.request.CommentPageRequest;
import com.mallangs.domain.comment.dto.request.CommentUpdateRequest;
import com.mallangs.domain.comment.dto.response.CommentResponse;
import com.mallangs.domain.comment.entity.Comment;
import com.mallangs.domain.comment.repository.CommentRepository;
import com.mallangs.domain.member.entity.Member;
import com.mallangs.domain.member.entity.MemberRole;
import com.mallangs.domain.member.repository.MemberRepository;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ArticleRepository articleRepository;

    // 댓글 등록
    private CommentResponse createComment(Member member, String content, Board board, Article article) {
        Comment comment = Comment.builder()
                .member(member)
                .content(content)
                .board(board)
                .article(article)
                .build();

        commentRepository.save(comment);
        return new CommentResponse(comment);
    }

    // 댓글 수정
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMember().getMemberId().equals(commentUpdateRequest.getMemberId())) {
            throw new MallangsCustomException(ErrorCode.FORBIDDEN_ACCESS);
        }
        try {
            comment.changeContent(commentUpdateRequest.getContent());
            return new CommentResponse(comment);
        } catch (Exception e) {
            throw new MallangsCustomException(ErrorCode.COMMENT_NOT_MODIFIED);
        }
    }

    // 댓글 삭제 (user, admin 공용)
    public void deleteComments(Long memberId, List<Long> commentIds) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        for (Long commentId : commentIds) {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new MallangsCustomException(ErrorCode.COMMENT_NOT_FOUND));

            if (member.getMemberRole().equals(MemberRole.ROLE_ADMIN) || comment.getMember().getMemberId().equals(memberId)) {
                try {
                    commentRepository.delete(comment);
                } catch (Exception e) {
                    throw new MallangsCustomException(ErrorCode.COMMENT_NOT_REMOVED);
                }
            } else {
                throw new MallangsCustomException(ErrorCode.FORBIDDEN_ACCESS);
            }
        }
    }

    // 게시글 별 댓글 목록 조회
    public Page<CommentResponse> getPostComments(Long boardId, Long articleId, CommentPageRequest commentPageRequest) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = commentPageRequest.getPageable(sort);
        Page<Comment> comments;

        if (articleId != null) {
            comments = commentRepository.findCommentsArticleId(articleId, pageable);
        }
        else if (boardId != null) {
            comments = commentRepository.findCommentsByBoardId(boardId, pageable);
        } else {
            throw new MallangsCustomException(ErrorCode.POST_NOT_FOUND);
        }

        if (comments.isEmpty()) {
            throw new MallangsCustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return comments.map(CommentResponse::new);
    }

    // 회원 댓글 목록 조회
    public Page<CommentResponse> getMyComments (Long memberId, CommentPageRequest commentPageRequest) {
        Sort sort = Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = commentPageRequest.getPageable(sort);
        Page<Comment> comments = commentRepository.findCommentsByMemberId(memberId, pageable);

        if (comments.isEmpty()) {
            throw new MallangsCustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return comments.map(CommentResponse::new);
    }

    // 관리자 전체 댓글 목록 조회
    public Page<CommentResponse> getAllComments(CommentPageRequest commentPageRequest) {
        Sort sort = Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = commentPageRequest.getPageable(sort);
        Page<Comment> comments = commentRepository.findAll(pageable);

        if (comments.isEmpty()) {
            throw new MallangsCustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return comments.map(CommentResponse::new);
    }

    // 닉네임 별 댓글 목록 조회
    public Page<CommentResponse> getCommentsByNickname(String nickname, CommentPageRequest commentPageRequest) {
        Sort sort = Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = commentPageRequest.getPageable(sort);
        Page<Comment> comments = commentRepository.findCommentsByNickname(nickname, pageable);

        if (comments.isEmpty()) {
            throw new MallangsCustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return comments.map(CommentResponse::new);
    }

    // 댓글 내용 별 댓글 목록 조회
    public Page<CommentResponse> getCommentsByContent(String content, CommentPageRequest commentPageRequest) {
        Sort sort = Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = commentPageRequest.getPageable(sort);
        Page<Comment> comments = commentRepository.findCommentsByContent(content, pageable);

        if (comments.isEmpty()) {
            throw new MallangsCustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return comments.map(CommentResponse::new);
    }

    // 댓글 생성 범위 별 댓글 목록 조회
    public Page<CommentResponse> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, CommentPageRequest commentPageRequest) {
        endDate = endDate.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        Sort sort = Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = commentPageRequest.getPageable(sort);
        Page<Comment> comments = commentRepository.findCommentsByDateRange(startDate, endDate, pageable);

        if (comments.isEmpty()) {
            throw new MallangsCustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return comments.map(CommentResponse::new);
    }

    // 커뮤니티 댓글 생성
    public CommentResponse createBoardComment(CommentBoardRequest commentBoardRequest) {
        Member member = memberRepository.findById(commentBoardRequest.getMemberId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        Board board = boardRepository.findById(commentBoardRequest.getBoardId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.BOARD_NOT_FOUND));

        return createComment(member, commentBoardRequest.getContent(), board, null);
    }

    // 글타래 댓글 생성
    public CommentResponse createArticleComment(CommentArticleRequest commentArticleRequest) {
        Member member = memberRepository.findById(commentArticleRequest.getMemberId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.MEMBER_NOT_FOUND));

        Article article = articleRepository.findById(commentArticleRequest.getArticleId())
                .orElseThrow(() -> new MallangsCustomException(ErrorCode.ARTICLE_NOT_FOUND));

        return createComment(member, commentArticleRequest.getContent(), null, article);
    }
}
