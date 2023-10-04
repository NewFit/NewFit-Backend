package com.newfit.reservation.controller;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.reservation.Reservation;
import com.newfit.reservation.dto.request.ObtainCreditRequest;
import com.newfit.reservation.dto.response.UserRankInfoListResponse;
import com.newfit.reservation.service.AuthorityService;
import com.newfit.reservation.service.CreditService;
import com.newfit.reservation.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credit")
@RequiredArgsConstructor
public class CreditApiController {
    private final CreditService creditService;
    private final ReservationService reservationService;
    private final AuthorityService authorityService;

    @GetMapping
    public ResponseEntity<UserRankInfoListResponse> getGymRanking(@RequestHeader(name = "authority-id") Long authorityId) {
        UserRankInfoListResponse gymRanking = creditService.getRankInGym(authorityId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gymRanking);
    }

    @PatchMapping
    public ResponseEntity<Void> finishEquipmentUseAndObtainCredit(@RequestHeader(name = "authority-id") Long authorityId,
                                                                                  @RequestParam(name = "reservation_id") Long reservationId,
                                                                                  @Valid @RequestBody ObtainCreditRequest requestDto) {
        Reservation reservation = reservationService.findById(reservationId);
        Authority authority = authorityService.findById(authorityId);

        reservationService.checkConditionAndAddCredit(reservation, authority, requestDto.getEndEquipmentUseAt());

        return ResponseEntity
                .noContent()
                .build();
    }
}
