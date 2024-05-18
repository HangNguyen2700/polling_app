package com.polling_app.polling_app.controller;

import com.polling_app.polling_app.dto.request.auth.LoginRequest;
import com.polling_app.polling_app.dto.request.auth.RegisterRequest;
import com.polling_app.polling_app.dto.response.DetailedErrorResponse;
import com.polling_app.polling_app.dto.response.ErrorResponse;
import com.polling_app.polling_app.dto.response.SuccessResponse;
import com.polling_app.polling_app.dto.response.auth.TokenResponse;
import com.polling_app.polling_app.service.AuthService;
import com.polling_app.polling_app.service.MessageSourceService;
import com.polling_app.polling_app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.polling_app.polling_app.util.Constants.SECURITY_SCHEME_NAME;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "001. Auth", description = "Auth API")
public class AuthController extends AbstractBaseController {
    private final AuthService authService;

    private final UserService userService;

    private final MessageSourceService messageSourceService;

    @PostMapping("/login")
    @Operation(
        summary = "Login endpoint",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad credentials",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Validation failed",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DetailedErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<TokenResponse> login(
        @Parameter(description = "Request body to login", required = true)
        @RequestBody @Validated final LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/register")
    @Operation(
        summary = "Register endpoint",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Validation failed",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DetailedErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SuccessResponse> register(
        @Parameter(description = "Request body to register", required = true)
        @RequestBody @Valid RegisterRequest request
    ) throws BindException {
        userService.register(request);

        return ResponseEntity.ok(SuccessResponse.builder().message(messageSourceService.get("registered_successfully"))
            .build());
    }

    @GetMapping("/logout")
    @Operation(
        summary = "Logout endpoint",
        security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad request",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SuccessResponse> logout() {
        authService.logout(userService.getUser());

        return ResponseEntity.ok(SuccessResponse.builder()
            .message(messageSourceService.get("logout_successfully"))
            .build());
    }
}
