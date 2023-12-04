package com.newfit.reservation.domains.reservation.repository;

import com.newfit.reservation.common.config.QuerydslConfig;
import com.newfit.reservation.domains.reservation.domain.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void authorityId로_reservation_모두_조회() {
        // given
        Long authorityId = 1L;

        // when
        List<Reservation> reservations = reservationRepository.findAllByAuthorityId(authorityId);

        // then
        assertThat(reservations.size()).isEqualTo(3); // db 내부 데이터 수에 따라 변경
        reservations.forEach(reservation ->
                assertThat(reservation.getAuthority().getId()).isEqualTo(authorityId));
    }
}