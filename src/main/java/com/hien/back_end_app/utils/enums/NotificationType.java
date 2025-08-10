package com.hien.back_end_app.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum NotificationType {
    @JsonProperty("message")
    MESSAGE,
    @JsonProperty("comment_post")
    COMMENT_POST,
    @JsonProperty("comment_reply")
    COMMENT_REPLY,
    @JsonProperty("post")
    POST,
    @JsonProperty("follow")
    FOLLOW,
    @JsonProperty("emotion")
    EMOTION,
    @JsonProperty("group_join")
    GROUP_JOIN,
    @JsonProperty("group_post")
    GROUP_POST,
    @JsonProperty("group_invite")
    GROUP_INVITE,
    @JsonProperty("general")
    GENERAL,
    UNKNOWN;

    @JsonCreator
    public static NotificationType from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return NotificationType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
