package com.polling_app.polling_app.controller;

import com.polling_app.polling_app.dto.response.ErrorResponse;
import com.polling_app.polling_app.dto.response.user.UserResponse;
import com.polling_app.polling_app.entity.Poll;
import com.polling_app.polling_app.service.PollService;
import com.polling_app.polling_app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.polling_app.polling_app.util.Constants.SECURITY_SCHEME_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/polls")
@Tag(name = "003. Poll", description = "Poll API")
public class PollController extends AbstractBaseController {
    private final PollService pollService;

    @GetMapping
    @Operation(
            summary = "Get all polls endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Poll.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Bad credentials",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<List<Poll>> getAllPolls(@RequestParam int limit, @RequestParam int offset) {
        return ResponseEntity.ok(pollService.getAllPolls(limit, offset));
    }

    @GetMapping
    @Operation(
            summary = "Get polls by keyword endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Poll.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Bad credentials",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<List<Poll>> getPollsByKeyword(@RequestParam String keyword, @RequestParam int limit, @RequestParam int offset) {
        return ResponseEntity.ok(pollService.getPollsByKeyword(keyword, limit, offset));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get poll by ID endpoint",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Poll.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Full authentication is required to access this resource",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Poll> getPollById(
            @Parameter(name = "id", description = "Poll ID", required = true)
            @PathVariable("id") final String id
    ) {
        return ResponseEntity.ok(pollService.findById(id));
    }
}
