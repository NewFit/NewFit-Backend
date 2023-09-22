package com.newfit.reservation.repository;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Credit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {

    List<Credit> findAllByAuthorityAndYearAndMonth(Authority authority,
                                                 Short year,
                                                 Short month);
}
