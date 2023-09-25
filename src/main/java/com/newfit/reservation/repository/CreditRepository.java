package com.newfit.reservation.repository;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Credit;
import com.newfit.reservation.domain.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {

    List<Credit> findAllByAuthorityAndYearAndMonth(Authority authority, Short year, Short month);

    @Query("select c from Credit c where c.authority.gym =:gym and c.year =:year and c.month =:month order by c.amount desc ")
    List<Credit> findAllByGymAndYearAndMonth(@Param("gym") Gym gym, @Param("year") Short year, @Param("month") Short month);

    Optional<Credit> findByAuthorityAndYearAndMonth(Authority authority, Short year, Short month);
}
