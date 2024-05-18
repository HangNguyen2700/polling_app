package com.polling_app.polling_app.dto.request.user;

import com.polling_app.polling_app.dto.annotation.FieldMatch;
import com.polling_app.polling_app.dto.annotation.MinListSize;
import com.polling_app.polling_app.dto.annotation.Password;
import com.polling_app.polling_app.dto.annotation.ValueOfEnum;
import com.polling_app.polling_app.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
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
@FieldMatch(first = "password", second = "passwordConfirm", message = "{password_mismatch}")
public class UpdateUserRequest extends AbstractBaseUpdateUserRequest {
    @Password(message = "{invalid_password}")
    @Schema(
        name = "password",
        description = "Password of the user",
        type = "String",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        example = "P@sswd123."
    )
    private String password;

    @Schema(
        name = "passwordConfirm",
        description = "Password for confirmation",
        type = "String",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        example = "P@sswd123."
    )
    private String passwordConfirm;

    @MinListSize(min = 1, message = "{min_list_size}")
    @Schema(
        name = "roles",
        description = "Roles of the user",
        type = "List<String>",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        allowableValues = {"ADMIN", "USER"},
        example = "[\"USER\"]"
    )
    private List<@ValueOfEnum(enumClass = Constants.RoleEnum.class) String> roles;
}
