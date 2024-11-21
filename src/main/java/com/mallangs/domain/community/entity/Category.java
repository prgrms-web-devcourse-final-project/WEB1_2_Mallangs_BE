package com.mallangs.domain.community.entity;

public enum Category {
    COMMUNITY("커뮤니티"),
    FEED("피드"),
    PLACE("장소"),
    RESCUE("구조"),
    LOST("실종"),
    SIGHED("발견");

    private String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
