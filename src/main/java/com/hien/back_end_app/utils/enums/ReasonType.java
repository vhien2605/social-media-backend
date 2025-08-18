package com.hien.back_end_app.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReasonType {
    VIOLENCE,
    SUICIDE_OR_SELF_HARM,
    TERRORISM,
    HATE_SPEECH,
    SEXUAL_CONTENT,
    HARASSMENT,
    SPAM,
    MISINFORMATION,
    SCAM_OR_FRAUD,
    FAKE_ACCOUNT,
    COPYRIGHT_VIOLATION,
    TRADEMARK_VIOLATION,
    UNKNOWN;

    @JsonCreator
    public static ReasonType from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return ReasonType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
