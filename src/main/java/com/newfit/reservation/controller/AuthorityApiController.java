package com.newfit.reservation.controller;

import com.newfit.reservation.dto.request.AuthorityRequest;
import com.newfit.reservation.dto.response.GymListResponse;
import com.newfit.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.service.AuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/authority")
@RequiredArgsConstructor
public class AuthorityApiController {
    private final AuthorityService authorityService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestHeader(value = "User-Id") Long userId,
                                   @Valid @RequestBody AuthorityRequest request) {

        authorityService.register(userId, request.getGymId());

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @GetMapping
    public GymListResponse listRegistration(@RequestHeader(value = "authority-id") Long authorityId) {
        return authorityService.listRegistration(authorityId);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestHeader(value = "authority-id") Long authorityId) {

        authorityService.delete(authorityId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/reservation")
    public ReservationListResponse listReservation(@RequestHeader("authority-id") Long authorityId) {

        return authorityService.listAuthorityReservation(authorityId);
    }
}
