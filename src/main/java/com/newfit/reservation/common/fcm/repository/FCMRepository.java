package com.newfit.reservation.common.fcm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newfit.reservation.common.fcm.domain.FCMToken;

public interface FCMRepository extends JpaRepository<FCMToken, Long> {
}
