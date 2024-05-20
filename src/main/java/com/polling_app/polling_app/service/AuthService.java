package com.polling_app.polling_app.service;

import com.polling_app.polling_app.dto.response.auth.TokenExpiresInResponse;
import com.polling_app.polling_app.dto.response.auth.TokenResponse;
import com.polling_app.polling_app.entity.User;
import com.polling_app.polling_app.exception.NotFoundException;
import com.polling_app.polling_app.security.JwtTokenProvider;
import com.polling_app.polling_app.security.JwtUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.polling_app.polling_app.util.Constants.TOKEN_HEADER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;

    private final JwtTokenService jwtTokenService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final HttpServletRequest httpServletRequest;

    private final MessageSourceService messageSourceService;

    /**
     * Authenticate user.
     *
     * @param username   String
     * @param password   String
     * @return TokenResponse
     */
    public TokenResponse login(String username, final String password) {
        log.info("Login request received: {}", username);

        String badCredentialsMessage = messageSourceService.get("bad_credentials");

        try {
            User user = userService.findByUsername(username);
//            username = user.getUsername(); no need to reassign username?!
        } catch (NotFoundException e) {
            log.error("User not found with username: {}", username);
            throw new AuthenticationCredentialsNotFoundException(badCredentialsMessage);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            JwtUserDetails jwtUserDetails = jwtTokenProvider.getPrincipal(authentication);

            return generateTokens(UUID.fromString(jwtUserDetails.getId()));
        } catch (NotFoundException e) {
            log.error("Authentication failed for email: {}", username);
            throw new AuthenticationCredentialsNotFoundException(badCredentialsMessage);
        }
    }

    /**
     * Logout from bearer string by user.
     *
     * @param user   User
     * @param bearer String
     */
    //TODO: complete logout
    public void logout(User user, final String bearer) {

    }

    /**
     * Logout from bearer string by user.
     *
     * @param user User
     */
    public void logout(User user) {
        logout(user, httpServletRequest.getHeader(TOKEN_HEADER));
    }

    /**
     * Generate both access and refresh tokens.
     *
     * @param id         user identifier to set the subject for token and value for the expiring map
     * @return an object of TokenResponse
     */
    private TokenResponse generateTokens(final UUID id) {
        String token = jwtTokenProvider.generateJwt(id.toString());

        log.info("Token generated for user: {}", id);

        return TokenResponse.builder()
            .token(token)
            .expiresIn(
                TokenExpiresInResponse.builder()
                    .token(jwtTokenProvider.getTokenExpiresIn())
                    .build()
            )
            .build();
    }
}
