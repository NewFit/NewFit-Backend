package com.newfit.reservation.domains.user.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDropRequest {

    @Email
    private String email;
}
