package com.hien.back_end_app.utils.validators;

public class ZeroFilter {
    @Override
    public boolean equals(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue() == 0L;
        }
        return false;
    }
}
