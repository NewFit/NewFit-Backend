package com.newfit.reservation.domains.credit.controller;

import com.newfit.reservation.domains.credit.dto.response.UserRankInfoListResponse;
import com.newfit.reservation.domains.credit.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credit")
@RequiredArgsConstructor
public class CreditApiController {
    private final CreditService creditService;

    @GetMapping
    public ResponseEntity<UserRankInfoListResponse> getGymRanking(@RequestHeader(name = "authority-id") Long authorityId) {
        UserRankInfoListResponse gymRanking = creditService.getRankInGym(authorityId);
        return ResponseEntity
                .ok(gymRanking);
    }
}
