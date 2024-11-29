package com.mallangs.domain.review.entity;

public enum ReviewStatus {
    PUBLISHED("게시"),
    HIDDEN("숨김");


    private final String koreanName;

    ReviewStatus(String koreanName) {
        this.koreanName = koreanName;
    }

}
