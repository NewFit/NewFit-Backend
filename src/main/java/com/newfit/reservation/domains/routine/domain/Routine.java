package com.newfit.reservation.domains.routine.domain;

import java.util.ArrayList;
import java.util.List;

import com.newfit.reservation.common.model.BaseTimeEntity;
import com.newfit.reservation.domains.authority.domain.Authority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Routine extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "authority_id")
	private Authority authority;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Long count;

	@OneToMany(mappedBy = "routine", cascade = CascadeType.REMOVE)
	private List<EquipmentRoutine> equipmentRoutineList = new ArrayList<>();

	public void updateName(String name) {
		this.name = name;
	}

	public void incrementCount() {
		this.count++;
	}

	@Builder(access = AccessLevel.PRIVATE)
	private Routine(Authority authority, String name) {
		this.authority = authority;
		this.name = name;
		this.count = 0L;
	}

	public static Routine createRoutine(Authority authority, String name) {
		return Routine.builder()
			.authority(authority)
			.name(name)
			.build();
	}
}
