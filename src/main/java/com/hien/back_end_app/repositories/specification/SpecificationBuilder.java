package com.hien.back_end_app.repositories.specification;

import com.hien.back_end_app.utils.enums.SearchOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class SpecificationBuilder<T extends SupportsSpecification> {
    private final List<SpecSearchCriteria> params;

    public SpecificationBuilder() {
        params = new ArrayList<>();
    }

    public SpecificationBuilder(List<SpecSearchCriteria> params) {
        this.params = params;
    }

    public SpecificationBuilder<T> with(String orPredicate, String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (searchOperation == null) {
            return this;
        }
        params.add(new SpecSearchCriteria(orPredicate, key, searchOperation, value, prefix, suffix));
        return this;
    }

    public SpecificationBuilder<T> with(GenericSpecification<T> spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public SpecificationBuilder<T> with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

    public Specification<T> build() {
        if (params.isEmpty()) {
            return null;
        }
        Specification<T> result = new GenericSpecification<>(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).getOrPredicate()
                    ? result.or(new GenericSpecification<>(params.get(i)))
                    : result.and(new GenericSpecification<>(params.get(i)));
        }
        return result;
    }
}

