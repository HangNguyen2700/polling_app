package com.polling_app.polling_app.dto.response.user;

import com.polling_app.polling_app.dto.response.AbstractBaseResponse;
import com.polling_app.polling_app.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class UserResponse extends AbstractBaseResponse {
    @Schema(
        name = "id",
        description = "UUID",
        type = "String",
        example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
        name = "username",
        description = "Username of the user",
        type = "String",
        example = "username"
    )
    private String username;

    @Schema(
        name = "displayName",
        description = "Name of the user",
        type = "String",
        example = "John"
    )
    private String displayName;

    @Schema(
        name = "roles",
        description = "role of the user",
        type = "List",
        example = "[\"USER\"]"
    )
    private List<String> roles;

    @Schema(
        name = "createdAt",
        description = "Date time field of user creation",
        type = "LocalDateTime",
        example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    /**
     * Convert User to UserResponse
     * @param user User
     * @return UserResponse
     */
    public static UserResponse convert(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .roles(user.getRoles().stream().map(role -> role.getName().name()).toList())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
