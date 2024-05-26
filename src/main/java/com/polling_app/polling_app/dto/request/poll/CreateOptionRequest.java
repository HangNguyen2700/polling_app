package com.polling_app.polling_app.dto.request.poll;

import com.polling_app.polling_app.entity.Option;
import com.polling_app.polling_app.entity.Poll;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public class CreateOptionRequest {
    @NotEmpty(message = "...")
    @Schema(
            name = "content",
            description = "content of an option",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    public static Option convert(CreateOptionRequest optionRequest, Poll poll) {
        return Option.builder()
                .content(optionRequest.content)
                .existIn(poll)
                .build();
    }
}
