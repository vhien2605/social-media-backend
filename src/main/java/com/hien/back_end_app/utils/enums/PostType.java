package com.hien.back_end_app.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum PostType {
    @JsonProperty("wall_post")
    WALL_POST,
    @JsonProperty("group_post")
    GROUP_POST,
    UNKNOWN;

    @JsonCreator
    public static PostType from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return PostType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
