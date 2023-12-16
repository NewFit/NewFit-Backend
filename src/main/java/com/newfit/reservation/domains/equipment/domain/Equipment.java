package com.newfit.reservation.domains.equipment.domain;

import java.util.ArrayList;
import java.util.List;

import com.newfit.reservation.common.model.BaseTimeEntity;
import com.newfit.reservation.domains.gym.domain.Gym;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Equipment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gym_id", nullable = false)
	private Gym gym;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PurposeType purposeType;

	@Column(nullable = false)
	private Boolean active;

	@OneToMany(mappedBy = "equipment")
	private List<EquipmentGym> equipmentGyms = new ArrayList<>();

	@Builder(access = AccessLevel.PRIVATE)
	private Equipment(Gym gym, String name, PurposeType purposeType) {
		this.gym = gym;
		this.name = name;
		this.purposeType = purposeType;
		this.active = true;
	}

	public static Equipment createEquipment(Gym gym, String name, PurposeType purposeType) {
		return Equipment.builder()
			.gym(gym)
			.name(name)
			.purposeType(purposeType)
			.build();
	}

	public void deactivate() {
		this.active = false;
	}
}
