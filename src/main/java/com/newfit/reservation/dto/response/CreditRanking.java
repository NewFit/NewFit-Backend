package com.newfit.reservation.dto.response;

import com.newfit.reservation.domain.Credit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreditRanking extends Credit{

    @Column(nullable = false)
    private Long rank;

}
