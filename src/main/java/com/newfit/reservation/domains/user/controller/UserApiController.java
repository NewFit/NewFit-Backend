package com.newfit.reservation.domains.user.controller;

import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.domains.user.dto.request.UserSignUpRequest;
import com.newfit.reservation.domains.user.dto.request.UserUpdateRequest;
import com.newfit.reservation.domains.user.dto.response.UserDetailResponse;
import com.newfit.reservation.domains.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {

    private final UserService userService;
    private final AuthorityCheckService authorityCheckService;

    @PatchMapping
    public ResponseEntity<Void> modify(Authentication authentication,
                                       HttpServletResponse response,
                                       @RequestHeader(value = "user-id") Long userId,
                                       @Valid @RequestBody UserUpdateRequest request) {
        authorityCheckService.validateByUserId(authentication, userId);
        userService.modify(userId, request, response);
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
        userService.signUp(oauthHistoryId, request, response);

        return ResponseEntity
                .status(CREATED)
                .build();
    }
}
