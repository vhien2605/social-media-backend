package com.hien.back_end_app.repositories.specification;

import com.hien.back_end_app.entities.Conversation;
import com.hien.back_end_app.entities.User;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.ru.INN;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;


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
        } else if (criteria.getKey().equalsIgnoreCase("idCreate")) {
            Join<T, User> join = root.join("createdBy", JoinType.INNER);
            return buildJoinUserFromPost(join, query, criteriaBuilder);
        } else if (criteria.getKey().equalsIgnoreCase("createdEmail")) {
            Join<T, User> join = root.join("createdBy", JoinType.INNER);
            return buildJoinEmailCreatedUserFromPost(join, query, criteriaBuilder);
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
            case IN -> {
                if (!(criteria.getValue() instanceof Collection<?> values)) {
                    throw new IllegalArgumentException("IN operator requires a Collection");
                }
                CriteriaBuilder.In<Object> in = builder.in(root.get(criteria.getKey()));
                for (Object v : values) {
                    in.value(v);
                }
                yield in;
            }
        };
    }

    private Predicate buildJoinUserPredicate(Join<T, ?> join, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.equal(builder.lower(join.get("email")), criteria.getValue().toString().toLowerCase());
    }

    private Predicate buildJoinConversationPredicate(Join<T, ?> join, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.equal(join.get("id"), criteria.getValue());
    }

    private Predicate buildJoinUserFromPost(Join<T, ?> join, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (!(criteria.getValue() instanceof Collection<?> values)) {
            throw new IllegalArgumentException("IN operator requires a Collection");
        }
        CriteriaBuilder.In<Object> in = builder.in(join.get("id"));
        for (Object v : values) {
            in.value(v);
        }
        return in;
    }

    private Predicate buildJoinEmailCreatedUserFromPost(Join<T, ?> join, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.equal(join.get("email"), criteria.getValue());
    }
}
