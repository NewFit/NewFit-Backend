package com.newfit.reservation.dto.request;

import com.newfit.reservation.domain.Proposal;
import com.newfit.reservation.domain.Report;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
