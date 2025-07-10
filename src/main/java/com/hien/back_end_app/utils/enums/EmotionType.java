package com.hien.back_end_app.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum EmotionType {
    @JsonProperty("like")
    LIKE,
    @JsonProperty("haha")
    HAHA,
    @JsonProperty("cry")
    CRY,
    @JsonProperty("cool")
    COOL,
    @JsonProperty("love")
    LOVE,
    @JsonProperty("mad")
    MAD,

    UNKNOWN;

    @JsonCreator
    public static EmotionType from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return EmotionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
