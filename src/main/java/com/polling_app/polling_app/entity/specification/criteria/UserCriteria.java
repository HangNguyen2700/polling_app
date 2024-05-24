package com.polling_app.polling_app.entity.specification.criteria;

import com.polling_app.polling_app.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class UserCriteria {
    private List<Constants.RoleEnum> roles;

    private LocalDateTime createdAtStart;

    private LocalDateTime createdAtEnd;

    private String q;
}
