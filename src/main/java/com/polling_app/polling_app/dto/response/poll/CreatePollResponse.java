package com.polling_app.polling_app.dto.response.poll;

import com.polling_app.polling_app.dto.annotation.MinListSize;
import com.polling_app.polling_app.entity.Poll;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePollResponse {
    @NotEmpty(message = "...")
    @Schema(
            name = "question",
            description = "question of a poll",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String question;

    @Schema(
            name = "expires at",
            description = "poll expries at",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime expiresAt;

    @Schema(
            name = "created at",
            description = "Date time field of poll creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @NotEmpty(message = "...")
    @MinListSize(min = 1, message = "...")
    @Schema(
            name = "option responses",
            description = "option responses of a poll",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Set<CreateOptionResponse> optionResponses;

    public static CreatePollResponse convert(Poll poll) {
        return CreatePollResponse.builder()
                .question(poll.getQuestion())
                .expiresAt(poll.getExpiresAt())
                .createdAt(poll.getCreatedAt())
                .optionResponses(poll.getOptions().stream().map(CreateOptionResponse::convert).collect(Collectors.toSet()))
                .build();
    }

}
