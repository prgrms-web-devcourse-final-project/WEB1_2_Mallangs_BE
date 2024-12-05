package com.mallangs.domain.review.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ReviewPageResponse {
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<ReviewInfoResponse> reviews;

    // Page 객체를 받아서 ReviewPageResponse 객체를 생성하는 생성자 추가
    public ReviewPageResponse(Page<ReviewInfoResponse> page) {
        this.currentPage = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
        this.pageSize = page.getSize();
        this.reviews = page.getContent();
    }
}