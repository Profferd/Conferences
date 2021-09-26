package com.hrushko.util;

public enum Constances {
    ID("id"),
    ROLE("role"),
    USER("user"),
    INCLUDE("includeView"),
    USER_INFO("userInfo"),
    EVENT("event"),
    PERMISSION("permission"),
    MENU("menu"),
    ERROR("error");

    private final String fieldName;

    Constances(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
