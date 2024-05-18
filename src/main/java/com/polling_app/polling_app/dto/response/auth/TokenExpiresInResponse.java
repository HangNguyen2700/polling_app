package com.polling_app.polling_app.dto.response.auth;

import com.polling_app.polling_app.dto.response.AbstractBaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TokenExpiresInResponse extends AbstractBaseResponse {
    @Schema(
        name = "token",
        description = "Token expires In",
        type = "Long",
        example = "3600"
    )
    private Long token;
}
