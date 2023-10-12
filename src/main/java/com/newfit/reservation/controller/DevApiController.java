package com.newfit.reservation.controller;

import com.newfit.reservation.common.RequestHeaderUtil;
import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.domain.User;
import com.newfit.reservation.dto.request.CreateProposalRequest;
import com.newfit.reservation.dto.request.CreateReportRequest;
import com.newfit.reservation.service.UserService;
import com.newfit.reservation.service.dev.ProposalService;
import com.newfit.reservation.service.dev.ReportService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final RequestHeaderUtil requestHeaderUtil;

    @PostMapping("/bug")
    public ResponseEntity<Void> createBugReport(Authentication authentication,
                                                HttpServletRequest servletRequest,
                                                @Valid @RequestBody CreateReportRequest request) {
        authorityCheckService.checkHeaderAndValidateAuthority(authentication, servletRequest);

        Long userId = requestHeaderUtil.extractUserId(servletRequest);
        User user = userService.findOneById(userId);
        reportService.saveReport(user, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/feature")
    public ResponseEntity<Void> createFeatureProposal(Authentication authentication,
                                                      HttpServletRequest servletRequest,
                                                      @Valid @RequestBody CreateProposalRequest request) {
        authorityCheckService.checkHeaderAndValidateAuthority(authentication, servletRequest);

        Long userId = requestHeaderUtil.extractUserId(servletRequest);
        User user = userService.findOneById(userId);
        proposalService.saveProposal(user, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
