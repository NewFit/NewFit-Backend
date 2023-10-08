package com.newfit.reservation.controller;

import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.request.CreateProposalRequest;
import com.newfit.reservation.dto.request.CreateReportRequest;
import com.newfit.reservation.service.UserService;
import com.newfit.reservation.service.dev.ProposalService;
import com.newfit.reservation.service.dev.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dev")
public class DevApiController {

    private final ReportService reportService;
    private final ProposalService proposalService;
    private final UserService userService;
    private final AuthorityCheckService authorityCheckService;

    @PostMapping("/bug")
    public ResponseEntity<Void> createBugReport(Authentication authentication,
                                                @RequestHeader(value = "user-id") Long userId,
                                                @Valid @RequestBody CreateReportRequest request) {
        authorityCheckService.validateByUserId(authentication, userId);

        User user = userService.findOneById(userId);
        reportService.saveReport(user, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/feature")
    public ResponseEntity<Void> createFeatureProposal(Authentication authentication,
                                                      @RequestHeader(value = "user-id") Long userId,
                                                      @Valid @RequestBody CreateProposalRequest request) {
        authorityCheckService.validateByUserId(authentication, userId);

        User user = userService.findOneById(userId);
        proposalService.saveProposal(user, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
