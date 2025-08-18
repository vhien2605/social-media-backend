package com.hien.back_end_app.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum ReportType {
    @JsonProperty("wall_comment_report")
    WALL_COMMENT_REPORT,
    @JsonProperty("wall_post_report")
    WALL_POST_REPORT,
    @JsonProperty("group_comment_report")
    GROUP_COMMENT_REPORT,
    @JsonProperty("group_post_report")
    GROUP_POST_REPORT,

    UNKNOWN;

    @JsonCreator
    public static ReportType from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return ReportType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
