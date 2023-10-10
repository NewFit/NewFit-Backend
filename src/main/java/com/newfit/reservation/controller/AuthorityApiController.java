package com.newfit.reservation.controller;

import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.common.auth.jwt.TokenProvider;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.request.AuthorityRequest;
import com.newfit.reservation.dto.request.EntryRequest;
import com.newfit.reservation.dto.response.GymListResponse;
import com.newfit.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.service.AuthorityService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/authority")
@RequiredArgsConstructor
public class AuthorityApiController {
    private final AuthorityService authorityService;
    private final AuthorityCheckService authorityCheckService;
    private final TokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<Void> register(Authentication authentication,
                                         @RequestHeader(value = "user-id") Long userId,
                                         @Valid @RequestBody AuthorityRequest request,
                                         HttpServletResponse response) {
        authorityCheckService.validateByUserId(authentication, userId);
        User user = authorityService.register(userId, request.getGymId());
        String accessToken = tokenProvider.generateAccessToken(user);
        log.info("AuthorityRegister.accessToken = {}", accessToken);
        response.setHeader("access-token", accessToken);

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<GymListResponse> listRegistration(@RequestHeader(value = "authority-id") Long authorityId) {
        GymListResponse gymListResponse = authorityService.listRegistration(authorityId);
        return ResponseEntity
                .ok(gymListResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(Authentication authentication,
                                       @RequestHeader(value = "authority-id") Long authorityId,
                                       HttpServletResponse response) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        User user = authorityService.delete(authorityId);
        String accessToken = tokenProvider.generateAccessToken(user);
        log.info("AuthorityDelete.accessToken = {}", accessToken);
        response.setHeader("access-token", accessToken);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/reservation")
    public ResponseEntity<ReservationListResponse> listReservation(@RequestHeader("authority-id") Long authorityId) {

        ReservationListResponse reservationListResponse = authorityService.listAuthorityReservation(authorityId);
        return ResponseEntity
                .ok(reservationListResponse);
    }

    @PatchMapping("/entry")
    public ResponseEntity<Void> enterGym(Authentication authentication,
                                         @RequestHeader("authority-id") Long authorityId,
                                         @Valid @RequestBody EntryRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        authorityService.enterGym(authorityId, request);
        return ResponseEntity
                .noContent()
                .build();
    }
}
