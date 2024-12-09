package com.mallangs.domain.comment.service;

import com.mallangs.domain.article.entity.Article;
import com.mallangs.domain.article.repository.ArticleRepository;
import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.entity.BoardType;
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
import com.mallangs.domain.notification.dto.NotificationSendDTO;
import com.mallangs.domain.notification.entity.NotificationType;
import com.mallangs.domain.notification.service.NotificationService;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import com.mallangs.global.handler.SseEmitters;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ArticleRepository articleRepository;
    private final NotificationService notificationService;
    private final SseEmitters sseEmitters;

    // 댓글 등록
    private CommentResponse createComment(Member member, String content, Board board, Article article) {
        log.info("Creating comment for memberId: {}, boardId: {}, articleId: {}",
                member.getMemberId(), (board != null ? board.getBoardId() : "N/A"),
                (article != null ? article.getId() : "N/A"));

        if (content.length() > 200) { // 길이 초과 체크 추가
            throw new IllegalArgumentException("댓글 내용은 200자 이하로 입력해야 합니다.");
        }

        try {
            Comment comment = Comment.builder()
                    .member(member)
                    .content(content)
                    .board(board)
                    .article(article)
                    .build();

            commentRepository.save(comment);
            log.info("Comment saved successfully: {}", comment);

            Long postMemberId = null;
            String notificationMessage = "";
            String notificationUrl = "";

            if (board != null) {
                postMemberId = board.getMember().getMemberId();
                notificationMessage = "[" + board.getTitle() + "] 새로운 댓글";
                if (board.getBoardType() == BoardType.COMMUNITY) {
                    notificationUrl = "/api/v1/board/community/" + board.getBoardId();
                } else if (board.getBoardType() == BoardType.SIGHTING) {
                    notificationUrl = "/api/v1/board/sighting/" + board.getBoardId();
                }
            }

            if (article != null) {
                postMemberId = article.getMember().getMemberId();
                notificationMessage = "[" + article.getTitle() + "] 새로운 댓글";
                notificationUrl = "/api/v1/articles/public/" + article.getId();
            }

            if (postMemberId != null && !postMemberId.equals(member.getMemberId())) {
                NotificationSendDTO notificationSendDTO = NotificationSendDTO.builder()
                        .memberId(postMemberId)
                        .message(notificationMessage)
                        .notificationType(NotificationType.COMMENT)
                        .url(notificationUrl)
                        .build();
                notificationService.send(notificationSendDTO);
            }

            String emitterId = postMemberId + "_";
            SseEmitter emitter = sseEmitters.findSingleEmitter(emitterId);
            log.info("Found emitter for memberId: {}", emitterId);

            if (emitter != null) {
                try {
                    emitter.send(new CommentResponse(comment));
                    log.info("Sent comment response to emitter for memberId: {}", emitterId);
                } catch (Exception e) {
                    log.error("Error sending comment response to emitter: {}", e.getMessage());
                    sseEmitters.delete(emitterId);
                }
            }

            return new CommentResponse(comment);
        } catch (Exception e) {
            throw new MallangsCustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
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

        return comments.map(CommentResponse::new);
    }

    // 회원 댓글 목록 조회
    public Page<CommentResponse> getMyComments (Long memberId, CommentPageRequest commentPageRequest) {
        Sort sort = Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = commentPageRequest.getPageable(sort);
        Page<Comment> comments = commentRepository.findCommentsByMemberId(memberId, pageable);


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
}