package com.polling_app.polling_app.dto.request.auth;

import com.polling_app.polling_app.dto.annotation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "{not_blank}")
    @Schema(
        name = "username",
        description = "Username of the user",
        type = "String",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "username"
    )
    private String username;

    @NotBlank(message = "{not_blank}")
    @Password(message = "{invalid_password}")
    @Schema(
        name = "password",
        description = "Password of the user",
        type = "String",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "P@sswd123."
    )
    private String password;
}
