package com.hien.back_end_app.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum CommentType {
    @JsonProperty("post_comment")
    POST_COMMENT,
    @JsonProperty("reply_comment")
    REPLY_COMMENT,

    UNKNOWN;

    @JsonCreator
    public static CommentType from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return CommentType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
