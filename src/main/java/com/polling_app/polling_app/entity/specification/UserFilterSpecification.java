package com.polling_app.polling_app.entity.specification;

import com.polling_app.polling_app.entity.Role;
import com.polling_app.polling_app.entity.User;
import com.polling_app.polling_app.entity.specification.criteria.UserCriteria;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class UserFilterSpecification implements Specification<User> {
    private final UserCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull final Root<User> root,
                                 @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder builder) {
        if (criteria == null) {
            return null;
        }

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getRoles() != null && !criteria.getRoles().isEmpty()) {
            Join<User, Role> roleJoin = root.join("roles");
            predicates.add(
                builder.in(roleJoin.get("name")).value(criteria.getRoles())
            );
        }

        if (criteria.getCreatedAtStart() != null) {
            predicates.add(
                builder.greaterThanOrEqualTo(root.get("createdAt"), criteria.getCreatedAtStart())
            );
        }

        if (criteria.getCreatedAtEnd() != null) {
            predicates.add(
                builder.lessThanOrEqualTo(root.get("createdAt"), criteria.getCreatedAtEnd())
            );
        }

        if (criteria.getQ() != null) {
            String q = String.format("%%%s%%", criteria.getQ().toLowerCase());

            predicates.add(
                builder.or(
                    builder.like(builder.lower(root.get("id").as(String.class)), q),
                    builder.like(builder.lower(root.get("email")), q),
                    builder.like(builder.lower(root.get("name")), q),
                    builder.like(builder.lower(root.get("lastName")), q)
                )
            );
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        return query.distinct(true).getRestriction();
    }
}
