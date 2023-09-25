package com.newfit.reservation.controller;

import com.newfit.reservation.dto.response.UserRankInfoListResponse;
import com.newfit.reservation.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/credit")
@RequiredArgsConstructor
public class CreditApiController {
    private final CreditService creditService;

    @GetMapping
    public ResponseEntity<UserRankInfoListResponse> getGymRanking(@RequestHeader(name = "authority-id") Long authorityId) {
        UserRankInfoListResponse gymRanking = creditService.getRankInGym(authorityId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gymRanking);
    }
}
