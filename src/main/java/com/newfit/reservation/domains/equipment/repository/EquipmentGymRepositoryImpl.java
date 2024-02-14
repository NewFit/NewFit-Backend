package com.newfit.reservation.domains.equipment.repository;

import static com.newfit.reservation.domains.equipment.domain.QEquipment.*;
import static com.newfit.reservation.domains.equipment.domain.QEquipmentGym.*;

import java.util.List;

import com.newfit.reservation.domains.equipment.domain.EquipmentGym;
import com.newfit.reservation.domains.equipment.domain.PurposeType;
import com.newfit.reservation.domains.equipment.dto.request.EquipmentQueryRequest;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EquipmentGymRepositoryImpl implements EquipmentGymRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<EquipmentGym> findAllByQuery(Gym gym, EquipmentQueryRequest request) {
		return queryFactory.selectFrom(equipmentGym)
			.join(equipmentGym.equipment, equipment).fetchJoin()
			.where(eqId(gym.getId()), queryOption(request))
			.fetch();
	}

	private BooleanExpression eqId(Long id) {
		return equipmentGym.gym.id.eq(id);
	}

	private BooleanBuilder queryOption(EquipmentQueryRequest request) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		return booleanBuilder
			.and(eqPurpose(request.purposeType()))
			.and(eqEquipmentId(request.equipmentId()));
	}

	private BooleanExpression eqEquipmentId(Long equipmentId) {
		return equipmentId == null ? null : equipmentGym.equipment.id.eq(equipmentId);
	}

	private BooleanExpression eqPurpose(PurposeType purposeType) {
		return purposeType == null ? null : equipment.purposeType.eq(purposeType);
	}
}
