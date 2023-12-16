package com.newfit.reservation.domains.equipment.repository;

import static com.newfit.reservation.domains.equipment.domain.QEquipment.*;
import static com.newfit.reservation.domains.equipment.domain.QEquipmentGym.*;

import java.util.List;

import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.domain.PurposeType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EquipmentGymRepositoryImpl implements EquipmentGymRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<EquipmentGym> findAllByGymAndPurpose(Long gymId, PurposeType purpose) {
		return queryFactory
			.selectFrom(equipmentGym)
			.join(equipmentGym.equipment, equipment).fetchJoin()
			.where(equipmentGym.gym.id.eq(gymId)
				.and(equipment.purposeType.eq(purpose)))
			.fetch();
	}
}
