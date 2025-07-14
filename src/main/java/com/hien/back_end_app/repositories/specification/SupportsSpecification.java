package com.hien.back_end_app.repositories.specification;

public interface SupportsSpecification {
    default String description() {
        return "This can support specification filter";
    }
}
