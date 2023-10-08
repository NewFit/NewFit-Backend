package com.newfit.reservation.controller;

import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.dto.request.AuthorityRequest;
import com.newfit.reservation.dto.request.EntryRequest;
import com.newfit.reservation.dto.response.GymListResponse;
import com.newfit.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.service.AuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/authority")
@RequiredArgsConstructor
public class AuthorityApiController {
    private final AuthorityService authorityService;
    private final AuthorityCheckService authorityCheckService;

    @PostMapping
    public ResponseEntity<Void> register(Authentication authentication,
                                         @RequestHeader(value = "user-id") Long userId,
                                         @Valid @RequestBody AuthorityRequest request) {
        authorityCheckService.validateByUserId(authentication, userId);
        authorityService.register(userId, request.getGymId());

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
                                       @RequestHeader(value = "authority-id") Long authorityId) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        authorityService.delete(authorityId);

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
