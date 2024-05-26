package com.polling_app.polling_app.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectSuccessResponse<T> {
    @Schema(
            name = "status",
            type = "Integer",
            description = "Object response status field",
            example = "200"
    )
    private int status;

    @Schema(
            name = "message",
            type = "String",
            description = "Response message field",
            example = "Success!"
    )
    private String message;

    @Schema(
            name = "data",
            type = "T",
            description = "Response data field",
            example = ""
    )
    private T data;
}
