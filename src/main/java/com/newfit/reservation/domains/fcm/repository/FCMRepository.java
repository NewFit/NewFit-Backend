package com.newfit.reservation.domains.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.domains.fcm.domain.FCMToken;

public interface FCMRepository extends JpaRepository<FCMToken, Long> {
}
