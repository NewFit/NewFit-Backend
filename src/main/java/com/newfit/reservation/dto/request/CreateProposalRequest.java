package com.newfit.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CreateProposalRequest {

    @NotNull
    @Length(min = 1, max = 30)
    private String name;

    @NotNull
    @Length(min = 1, max = 300)
    private String content;
}
