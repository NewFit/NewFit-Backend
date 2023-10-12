package com.newfit.reservation.common;

import com.newfit.reservation.service.AuthorityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestHeaderUtil {

    private final AuthorityService authorityService;

    public Long extractUserId(HttpServletRequest request) {
        if (request.getHeader("user-id") != null) {
            return Long.parseLong(request.getHeader("user-id"));
        } else {
            Long authorityId = Long.parseLong(request.getHeader("authority-id"));
            return authorityService.findById(authorityId).getUser().getId();
        }
    }
}
