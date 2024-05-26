package com.polling_app.polling_app.dto.request.poll;

import com.polling_app.polling_app.dto.annotation.MinListSize;
import com.polling_app.polling_app.entity.Poll;
import com.polling_app.polling_app.entity.User;
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
public class CreatePollRequest {
    @NotEmpty(message = "...")
    @Schema(
            name = "question",
            description = "question of a poll",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String question;

    @Schema(
            name = "expires at",
            description = "poll expires at",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime expiresAt;

    @NotEmpty(message = "...")
    @MinListSize(min = 1, message = "...")
    @Schema(
            name = "option requests",
            description = "option requests of a poll",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Set<CreateOptionRequest> options;

    public static Poll convert (CreatePollRequest createPollRequest, User currentUser) {
         Poll poll = Poll.builder()
                .question(createPollRequest.question)
                .createdBy(currentUser)
                .expiresAt(createPollRequest.expiresAt)
                .build();

         poll.setOptions(createPollRequest.getOptions().stream().map(option -> CreateOptionRequest.convert(option, poll)).collect(Collectors.toSet()));

         return poll;
    }
}
