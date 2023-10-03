package com.newfit.reservation.controller;

import com.newfit.reservation.dto.request.AuthorityRequest;
import com.newfit.reservation.dto.response.GymListResponse;
import com.newfit.reservation.dto.response.ReservationListResponse;
import com.newfit.reservation.service.AuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@ResponseStatus(HttpStatus.OK)
@RestController
@RequestMapping("/api/v1/authority")
@RequiredArgsConstructor
public class AuthorityApiController {
    private final AuthorityService authorityService;

    @PostMapping
    public Long register(@RequestHeader(value = "User-Id") Long userId,
                         @Valid @RequestBody AuthorityRequest request) {
        return authorityService.register(userId, request.getGymId());
    }

    @GetMapping
    public GymListResponse listRegistration(@RequestHeader(value = "authority-id") Long authorityId) {
        return authorityService.listRegistration(authorityId);
    }

    @DeleteMapping
    public void delete(@RequestHeader(value = "authority-id") Long authorityId) {
        authorityService.delete(authorityId);
    }

    @GetMapping("/reservation")
    public ReservationListResponse listReservation(@RequestHeader("authority-id") Long authorityId) {

        return authorityService.listAuthorityReservation(authorityId);
    }
}
