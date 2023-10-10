package com.newfit.reservation.controller;

import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.common.auth.jwt.TokenProvider;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.request.UserSignUpRequest;
import com.newfit.reservation.dto.request.UserUpdateRequest;
import com.newfit.reservation.dto.response.UserDetailResponse;
import com.newfit.reservation.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {

    private final UserService userService;
    private final AuthorityCheckService authorityCheckService;
    private final TokenProvider tokenProvider;

    @PatchMapping
    public ResponseEntity<Void> modify(Authentication authentication,
                                       @RequestHeader(value = "user-id") Long userId,
                                       @Valid @RequestBody UserUpdateRequest request) {
        authorityCheckService.validateByUserId(authentication, userId);
        userService.modify(userId, request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping
    public ResponseEntity<UserDetailResponse> userDetail(@RequestHeader(value = "authority-id") Long authorityId) {
        UserDetailResponse userDetailResponse = userService.userDetail(authorityId);
        return ResponseEntity
                .ok(userDetailResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> drop(Authentication authentication,
                                     @RequestHeader(value = "user-id") Long userId) {
        authorityCheckService.validateByUserId(authentication, userId);
        userService.drop(userId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestHeader(value = "oauth-history-id") Long oauthHistoryId,
                                       @Valid @RequestBody UserSignUpRequest request,
                                       HttpServletResponse response) {
        User user = userService.signUp(oauthHistoryId, request);
        String accessToken = tokenProvider.generateAccessToken(user);
        log.info("accessToken = {}", accessToken);
        response.setHeader("access-token", accessToken);
        return ResponseEntity
                .status(CREATED)
                .build();
    }
}
