package com.newfit.reservation.dto.request;

import com.newfit.reservation.domain.dev.Proposal;
import lombok.Getter;

@Getter
public class CreateProposalRequestDto {

    private String name;
    private String content;

    // Dto -> Entity 변환 메소드입니다.
    public static Proposal proposalDto2Entity(CreateProposalRequestDto proposalRequestDto) {
        return Proposal.builder()
                .name(proposalRequestDto.getName())
                .content(proposalRequestDto.getContent())
                .build();
    }
}
