package com.newfit.reservation.domains.credit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.newfit.reservation.domains.authority.domain.Authority;
import com.newfit.reservation.domains.credit.domain.Credit;
import com.newfit.reservation.domains.credit.dto.response.CreditRanking;

public interface CreditRepository extends JpaRepository<Credit, Long> {

	List<Credit> findAllByAuthorityAndYearAndMonth(Authority authority, Short year, Short month);

	@Query(value = "SELECT * " +
		"FROM (SELECT *, dense_rank() over (order by amount desc) as rank " +
		"   FROM CREDIT " +
		"   WHERE gym_id = :gymId " +
		"       AND credit_year = :year " +
		"       AND credit_month = :month " +
		"   ORDER BY rank) as c " +
		"WHERE c.rank <= 10", nativeQuery = true)
	List<CreditRanking> findTop10ByGymIdAndYearAndMonth(@Param("gymId") Long gymId,
		@Param("year") Short year,
		@Param("month") Short month);

	@Query(value = "SELECT * " +
		"FROM (SELECT *, dense_rank() over (order by amount desc) as rank " +
		"   FROM CREDIT " +
		"   WHERE gym_Id = :gymId " +
		"       AND credit_year = :year " +
		"       AND credit_month = :month " +
		"   ORDER BY rank) as c " +
		"WHERE c.authority_id = :authorityId", nativeQuery = true)
	Optional<CreditRanking> findRank(@Param("authorityId") Long authorityId,
		@Param("gymId") Long gymId,
		@Param("year") Short year,
		@Param("month") Short month);

	Optional<Credit> findByAuthorityAndYearAndMonth(Authority authority, Short year, Short month);
}
