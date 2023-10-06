package com.newfit.reservation.service;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.dto.response.GymListResponse;
import com.newfit.reservation.dto.response.GymResponse;
import com.newfit.reservation.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GymService {
    private final GymRepository gymRepository;

    public Long save(Gym gym) {
        return gymRepository.save(gym).getId();
    }

    /*
    GymRepository로부터 NewFit 서비스에 등록된 모든 헬스장을 list 형태로 받습니다.
    그 다음에 Dto로 변환하여 Controller에게 반환합니다.
     */
    public GymListResponse getAllGyms() {
        List<Gym> findGyms = gymRepository.findAll();

        List<GymResponse> gyms = findGyms.stream()
                .map(GymResponse::new)
                .collect(Collectors.toList());

        return GymListResponse.builder()
                .gyms(gyms)
                .build();
    }
}
