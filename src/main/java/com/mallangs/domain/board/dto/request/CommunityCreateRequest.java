package com.mallangs.domain.board.dto.request;

import com.mallangs.domain.board.entity.Board;
import com.mallangs.domain.board.entity.BoardType;
import com.mallangs.domain.board.entity.Category;
import com.mallangs.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityCreateRequest {

    @NotNull(message = "카테고리를 선택하세요.")
    private Long categoryId;

    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    private String imgUrl;

    public Board toEntity(Member member, Category category) {
        return Board.builder()
                .member(member)
                .category(category)
                .title(this.title)
                .content(this.content)
                .imgUrl(this.imgUrl)
                .boardType(BoardType.COMMUNITY)
                .build();
    }
}
