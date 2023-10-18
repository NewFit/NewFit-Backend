package com.newfit.reservation.domains.credit.dto.response;

import com.newfit.reservation.domains.credit.domain.Credit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditRanking extends Credit {

    @Column(nullable = false)
    private Long rank;

}
