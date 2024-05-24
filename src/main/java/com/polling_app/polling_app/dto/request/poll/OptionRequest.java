package com.polling_app.polling_app.dto.request.poll;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public class OptionRequest {
    @NotEmpty(message = "...")
    @Schema(
            name = "content",
            description = "content of an option",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;
}
