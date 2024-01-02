package com.newfit.reservation.domains.gym.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.gym.dto.response.GymListResponse;
import com.newfit.reservation.domains.gym.dto.response.GymResponse;
import com.newfit.reservation.domains.gym.repository.GymRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymService {
	private final GymRepository gymRepository;

	public GymListResponse searchGyms(String gymName) {
		List<String> keywords = processQueryParam(gymName);
		List<Gym> findGyms = gymRepository.findAllByNameContaining(keywords);
		List<GymResponse> gyms = findGyms.stream()
			.map(GymResponse::new).toList();

		return GymListResponse.createResponse(gyms);
	}

	private List<String> processQueryParam(String gymName) {
		if (gymName == null || gymName.trim().equals("헬스장")) {
			return new ArrayList<>();
		}
		return Arrays.stream(gymName.replace("헬스장", "").trim().split("\s+")).toList();
	}
}
