package com.polling_app.polling_app.service;

import com.polling_app.polling_app.dto.request.auth.RegisterRequest;
import com.polling_app.polling_app.dto.request.user.AbstractBaseCreateUserRequest;
import com.polling_app.polling_app.dto.request.user.AbstractBaseUpdateUserRequest;
import com.polling_app.polling_app.dto.request.user.CreateUserRequest;
import com.polling_app.polling_app.dto.request.user.UpdateUserRequest;
import com.polling_app.polling_app.entity.User;
import com.polling_app.polling_app.entity.specification.UserFilterSpecification;
import com.polling_app.polling_app.entity.specification.criteria.PaginationCriteria;
import com.polling_app.polling_app.entity.specification.criteria.UserCriteria;
import com.polling_app.polling_app.exception.NotFoundException;
import com.polling_app.polling_app.repository.UserRepository;
import com.polling_app.polling_app.security.JwtUserDetails;
import com.polling_app.polling_app.util.Constants;
import com.polling_app.polling_app.util.PageRequestBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MessageSourceService messageSourceService;

    private final RoleService roleService;

    /**
     * Get authentication.
     *
     * @return Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Return the authenticated user.
     *
     * @return user User
     */
    public User getUser() {
        Authentication authentication = getAuthentication();
        if (authentication.isAuthenticated()) {
            try {
                return findById(getPrincipal(authentication).getId());
            } catch (ClassCastException | NotFoundException e) {
                log.warn("[JWT] User details not found!");
                throw new BadCredentialsException(messageSourceService.get("bad_credentials"));
            }
        } else {
            log.warn("[JWT] User not authenticated!");
            throw new BadCredentialsException(messageSourceService.get("bad_credentials"));
        }
    }

    /**
     * Count users.
     *
     * @return Long
     */
    public long count() {
        return userRepository.count();
    }

    /**
     * Find all users with pagination.
     *
     * @param criteria           UserCriteria
     * @param paginationCriteria PaginationCriteria
     * @return Page
     */
    public Page<User> findAll(UserCriteria criteria, PaginationCriteria paginationCriteria) {
        return userRepository.findAll(new UserFilterSpecification(criteria),
            PageRequestBuilder.build(paginationCriteria));
    }

    /**
     * Find a user by id.
     *
     * @param id UUID
     * @return User
     */
    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                new String[]{messageSourceService.get("user")})));
    }

    /**
     * Find a user by id.
     *
     * @param id String
     * @return User
     */
    public User findById(String id) {
        return findById(UUID.fromString(id));
    }

    /**
     * Find a user by username.
     *
     * @param username String.
     * @return User
     */
    public User findByUsername(final String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                new String[]{messageSourceService.get("user")})));
    }

    /**
     * Load user details by username.
     *
     * @param username String
     * @return UserDetails
     * @throws UsernameNotFoundException username not found exception.
     */
    public UserDetails loadUserByUsername(final String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                new String[]{messageSourceService.get("user")})));

        return JwtUserDetails.create(user);
    }

    /**
     * Loads user details by UUID string.
     *
     * @param id String
     * @return UserDetails
     */
    public UserDetails loadUserById(final String id) {
        User user = userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                new String[]{messageSourceService.get("user")})));

        return JwtUserDetails.create(user);
    }

    /**
     * Get UserDetails from security context.
     *
     * @param authentication Wrapper for security context
     * @return the Principal being authenticated or the authenticated principal after authentication.
     */
    public JwtUserDetails getPrincipal(final Authentication authentication) {
        return (JwtUserDetails) authentication.getPrincipal();
    }

    /**
     * Register user.
     *
     * @param request RegisterRequest
     * @return User
     */
    public User register(final RegisterRequest request) throws BindException {
        log.info("Registering user with username: {}", request.getUsername());

        User user = createUser(request);
        user.setRoles(List.of(roleService.findByName(Constants.RoleEnum.USER)));
        userRepository.save(user);

        log.info("User registered with username: {}, {}", user.getUsername(), user.getId());

        return user;
    }

    /**
     * Create user.
     *
     * @param request CreateUserRequest
     * @return User
     */
    public User create(final CreateUserRequest request) throws BindException {
        log.info("Creating user with username: {}", request.getUsername());

        User user = createUser(request);
        request.getRoles().forEach(role -> user.getRoles()
                .add(roleService.findByName(Constants.RoleEnum.get(role))));

        userRepository.save(user);

        log.info("User created with username: {}, {}", user.getUsername(), user.getId());

        return user;
    }

    /**
     * Update user.
     *
     * @param id      UUID
     * @param request UpdateUserRequest
     * @return User
     */
    public User update(UUID id, UpdateUserRequest request) throws BindException {
        User user = findById(id);
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getDisplayName());

        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRoles() != null) {
            user.setRoles(request.getRoles().stream()
                    .map(role -> roleService.findByName(Constants.RoleEnum.get(role)))
                    .collect(Collectors.toList()));
        }

        return updateUser(user, request);
    }

    /**
     * Update user.
     *
     * @param id      String
     * @param request UpdateUserRequest
     * @return User
     */
    public User update(String id, UpdateUserRequest request) throws BindException {
        return update(UUID.fromString(id), request);
    }

    /**
     * Delete user.
     *
     * @param id UUID
     */
    public void delete(String id) {
        userRepository.delete(findById(id));
    }

    /**
     * Create user.
     *
     * @param request AbstractBaseCreateUserRequest
     * @return User
     */
    private User createUser(AbstractBaseCreateUserRequest request) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        userRepository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    log.error("User with username: {} already exists", request.getUsername());
                    bindingResult.addError(new FieldError(bindingResult.getObjectName(), "username",
                            messageSourceService.get("unique_email")));
                });

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .build();
    }

    /**
     * Update user.
     *
     * @param user    User
     * @param request UpdateUserRequest
     * @return User
     */
    private User updateUser(User user, AbstractBaseUpdateUserRequest request) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        if (!user.getUsername().equals(request.getUsername()) &&
                userRepository.existsByUsernameAndIdNot(request.getUsername(), user.getId())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "email",
                    messageSourceService.get("already_exists")));
        }

        if (StringUtils.hasText(request.getDisplayName()) && !request.getDisplayName().equals(user.getDisplayName())) {
            user.setDisplayName(request.getDisplayName());
        }

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        userRepository.save(user);

        return user;
    }
}
