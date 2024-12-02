package com.mallangs.domain.comment.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mallangs.domain.comment.entity.Comment;
import com.mallangs.domain.member.entity.embadded.Nickname;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponse {
    private Long commentId;
    private Long memberId;
    private String content;
    private Nickname nickname;
    private String profileImage;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    private Long postId;
    private String postType;
    private String postTitle;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.memberId = comment.getMember().getMemberId();
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.profileImage = comment.getMember().getProfileImage();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();

        if (comment.getBoard() != null) {
            this.postId = comment.getBoard().getBoardId();
            this.postType = comment.getBoard().getBoardType().name();
            this.postTitle = comment.getBoard().getTitle();
        } else if (comment.getArticle() != null) {
            this.postId = comment.getArticle().getId();
            this.postType = comment.getArticle().getType();
            this.postTitle = comment.getArticle().getTitle();
        }
    }
}
