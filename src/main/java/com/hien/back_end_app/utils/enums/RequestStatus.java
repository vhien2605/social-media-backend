package com.hien.back_end_app.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum RequestStatus {
    @JsonProperty("pending")
    PENDING,

    @JsonProperty("accepted")
    ACCEPTED,

    @JsonProperty("rejected")
    REJECTED,

    UNKNOWN;

    @JsonCreator
    public static RequestStatus from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return RequestStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
