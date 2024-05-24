package com.polling_app.polling_app.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractBaseUpdateUserRequest {
    @NotBlank(message = "{not_blank}")
    @Size(max = 100, message = "{max_length}")
    @Schema(
            name = "username",
            description = "Username of the user",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "username"
    )
    private String username;

    @NotBlank(message = "{not_blank}")
    @Size(max = 50, message = "{max_length}")
    @Schema(
            name = "name",
            description = "Name of the user",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "John"
    )
    private String displayName;
}
