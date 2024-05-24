package com.polling_app.polling_app.dto.request.user;

import com.polling_app.polling_app.dto.annotation.MinListSize;
import com.polling_app.polling_app.dto.annotation.ValueOfEnum;
import com.polling_app.polling_app.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CreateUserRequest extends AbstractBaseCreateUserRequest {
    @NotEmpty(message = "{not_blank}")
    @MinListSize(min = 1, message = "{min_list_size}")
    @Schema(
        name = "roles",
        description = "Roles of the user",
        type = "List<String>",
        requiredMode = Schema.RequiredMode.REQUIRED,
        allowableValues = {"ADMIN", "USER"},
        example = "[\"USER\"]"
    )
    private List<@ValueOfEnum(enumClass = Constants.RoleEnum.class) String> roles;
}
