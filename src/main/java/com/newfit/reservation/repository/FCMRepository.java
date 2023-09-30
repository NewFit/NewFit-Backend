package com.newfit.reservation.repository;

import com.newfit.reservation.domain.common.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FCMRepository extends JpaRepository<FCMToken, Long> {
}
