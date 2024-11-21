package com.mallangs.domain.community.entity;

public enum Category {

    // 커뮤니티 카테코리명 임시 지정, 이름 나오면 수정 예정
    CATEGORY1("목격"),
    CATEGORY2("카테고리2"),
    CATEGORY3("카테고리3"),
    CATEGORY4("카테고리4"),
    CATEGORY5("카테고리5"),
    CATEGORY6("카테고리6");

    private String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}