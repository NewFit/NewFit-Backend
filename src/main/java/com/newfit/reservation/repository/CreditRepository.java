package com.newfit.reservation.repository;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Credit;
import com.newfit.reservation.dto.response.CreditRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {

    List<Credit> findAllByAuthorityAndYearAndMonth(Authority authority, Short year, Short month);

    @Query(value = "SELECT * " +
            "FROM (SELECT *, dense_rank() over (order by amount desc) as rank " +
            "   FROM CREDIT " +
            "   WHERE gym_id = :gymId " +
            "       AND credit_year = :year " +
            "       AND credit_month = :month " +
            "   ORDER BY rank)" +
            "WHERE rank <= 10", nativeQuery = true)
    List<CreditRanking> findHighRankByGymIdAndYearAndMonth(@Param("gymId") Long gymId,
                                                           @Param("year") Short year,
                                                           @Param("month") Short month);

    @Query(value = "SELECT * " +
            "FROM (SELECT *, dense_rank() over (order by amount desc) as rank " +
            "   FROM CREDIT " +
            "   WHERE gym_Id = :gymId " +
            "       AND credit_year = :year " +
            "       AND credit_month = :month " +
            "   ORDER BY rank)" +
            "WHERE authority_id = :authorityId", nativeQuery = true)
    Optional<CreditRanking> findRank(@Param("authorityId") Long authorityId,
                                     @Param("gymId") Long gymId,
                                     @Param("year") Short year,
                                     @Param("month") Short month);

    Optional<Credit> findByAuthorityAndYearAndMonth(Authority authority, Short year, Short month);
}
