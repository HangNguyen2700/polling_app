package com.polling_app.polling_app.security;

import com.polling_app.polling_app.entity.JwtToken;
import com.polling_app.polling_app.exception.NotFoundException;
import com.polling_app.polling_app.service.JwtTokenService;
import com.polling_app.polling_app.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;

import static com.polling_app.polling_app.util.Constants.TOKEN_HEADER;
import static com.polling_app.polling_app.util.Constants.TOKEN_TYPE;

@Component
@Slf4j
public class JwtTokenProvider {
    private final UserService userService;
    private final String appSecret;
    private final Long tokenExpiresIn;
    private final Long refreshTokenExpiresIn;
    private final Long rememberMeTokenExpiresIn;
    private final JwtTokenService jwtTokenService;
    private final HttpServletRequest httpServletRequest;

    public JwtTokenProvider(
            @Value("${app.secret}") final String appSecret,
            @Value("${app.jwt.token.expires-in}") final Long tokenExpiresIn,
            @Value("${app.jwt.refresh-token.expires-in}") final Long refreshTokenExpiresIn,
            @Value("${app.jwt.remember-me.expires-in}") final Long rememberMeTokenExpiresIn,
            final UserService userService,
            final JwtTokenService jwtTokenService,
            final HttpServletRequest httpServletRequest
    ) {
        this.userService = userService;
        this.appSecret = appSecret;
        this.tokenExpiresIn = tokenExpiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.rememberMeTokenExpiresIn = rememberMeTokenExpiresIn;
        this.jwtTokenService = jwtTokenService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * Extract jwt from request.
     *
     * @param request HttpServletRequest object to get Authorization header
     * @return String value of bearer token or null
     */
    public String extractJwtFromRequest(final HttpServletRequest request) {
        return extractJwtFromBearerString(request.getHeader(TOKEN_HEADER));
    }

    /**
     * Extract jwt from bearer string.
     *
     * @param bearer String
     * @return String value of bearer token or null
     */
    public String extractJwtFromBearerString(final String bearer) {
        if(StringUtils.hasText(bearer) && bearer.startsWith(String.format("%s", TOKEN_TYPE))){
            return bearer.substring(TOKEN_TYPE.length() + 1);
        }
        return null;
    }

    /**
     * Validate token.
     *
     * @param token              String
     * @param httpServletRequest HttpServletRequest
     * @return boolean
     */
//    public boolean validateToken(final String token, final HttpServletRequest httpServletRequest){
//        boolean isTokenValid = validateToken(token, true);
//    }
//
//    public boolean validateToken(final String token, final boolean isHttp) {
//        parseToken(token);
//        try {
//            JwtToken jwtToken = jwtTokenService.findByTokenOrRefreshToken(token);
//            if (isHttp && !httpServletRequest.getHeader("User-agent").equals(jwtToken.getUserAgent())) {
//                log.error("[JWT] User-agent is not matched");
//                return false;
//            }
//        } catch (NotFoundException e) {
//            log.error("[JWT] Token could not found in Redis");
//            return false;
//        }
//        return !isTokenExpired(token);
//    }

    private Jws<Claims> parseToken(final String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(appSecret.getBytes());
    }
}
