package com.polling_app.polling_app.dto.response.auth;

import com.polling_app.polling_app.dto.response.AbstractBaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TokenResponse extends AbstractBaseResponse {
    @Schema(
        name = "token",
        description = "Token",
        type = "String",
        example = "eyJhbGciOiJIUzUxMiJ9..."
    )
    private String token;

    @Schema(
        name = "expiresIn",
        description = "Expires In",
        type = "TokenExpiresInResponse"
    )
    private TokenExpiresInResponse expiresIn;
}
