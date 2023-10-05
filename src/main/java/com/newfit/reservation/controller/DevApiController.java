package com.newfit.reservation.controller;

import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.request.CreateProposalRequest;
import com.newfit.reservation.dto.request.CreateReportRequest;
import com.newfit.reservation.service.UserService;
import com.newfit.reservation.service.dev.ProposalService;
import com.newfit.reservation.service.dev.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dev")
public class DevApiController {

    private final ReportService reportService;
    private final ProposalService proposalService;
    private final UserService userService;

    @PostMapping("/bug")
    public ResponseEntity<Void> createBugReport(@RequestBody CreateReportRequest request) {
        // TODO: remove this userId and apply security
        Long userId = 1L;
        User user = userService.findOneById(userId);
        reportService.saveReport(user, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/feature")
    public ResponseEntity<Void> createFeatureProposal(@RequestBody CreateProposalRequest request) {
        // TODO: remove this userId and apply security
        Long userId = 1L;
        User user = userService.findOneById(userId);
        proposalService.saveProposal(user, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
