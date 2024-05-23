package com.polling_app.polling_app.dto.request.poll;

import com.polling_app.polling_app.dto.annotation.MinListSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

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

    @NotEmpty(message = "...")
    @MinListSize(min = 1, message = "...")
    @Schema(
            name = "option requests",
            description = "option requests of a poll",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Set<OptionRequest> optionRequests;
}
