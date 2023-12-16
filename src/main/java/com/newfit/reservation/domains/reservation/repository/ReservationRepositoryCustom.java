package com.newfit.reservation.domains.reservation.repository;

import java.util.List;

import com.newfit.reservation.domains.reservation.domain.Reservation;

public interface ReservationRepositoryCustom {
	List<Reservation> findAllByAuthorityId(Long authorityId);
}