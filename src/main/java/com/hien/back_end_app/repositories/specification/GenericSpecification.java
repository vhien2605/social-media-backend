package com.hien.back_end_app.repositories.specification;

import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.entities.User;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;


@RequiredArgsConstructor
@Getter
public class GenericSpecification<T extends SupportsSpecification> implements Specification<T> {
    private final SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getKey().equalsIgnoreCase("email")) {
            Join<T, User> join = root.join("user", JoinType.INNER);
            return buildJoinUserPredicate(join, query, criteriaBuilder);
        } else if (criteria.getKey().equalsIgnoreCase("conversation")) {
            Join<T, Conversation> join = root.join("conversation", JoinType.INNER);
            return buildJoinConversationPredicate(join, query, criteriaBuilder);
        }
        return buildNormalPredicate(root, query, criteriaBuilder);
    }
    //other Predicate join field for later requirements

    private Predicate buildNormalPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        };
    }

    private Predicate buildJoinUserPredicate(Join<T, ?> join, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.equal(builder.lower(join.get("email")), criteria.getValue().toString().toLowerCase());
    }

    private Predicate buildJoinConversationPredicate(Join<T, ?> join, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.equal(join.get("id"), criteria.getValue());
    }
}
