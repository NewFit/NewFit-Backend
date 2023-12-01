package com.newfit.reservation.domains.reservation.repository;

import com.newfit.reservation.domains.reservation.domain.Reservation;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> findAllByAuthorityId(Long authorityId);
}