package com.newfit.reservation.service;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.dto.response.GymListResponse;
import com.newfit.reservation.dto.response.GymResponse;
import com.newfit.reservation.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GymService {
    private final GymRepository gymRepository;

    public GymListResponse searchGyms(String gymName) {
        String keywordString = processQueryParam(gymName);
        List<Gym> findGyms = gymRepository.findAllByNameContaining(keywordString);
        List<GymResponse> gyms = findGyms.stream()
                .map(GymResponse::new)
                .toList();

        return GymListResponse.createGymListResponse(gyms);
    }

    private String processQueryParam(String gymName) {
        if (gymName == null) {
            return "'()'";
        }
        String processedGymName = gymName.replace("헬스장", "").trim().replaceAll("\s+", "|");
        return "'(" + processedGymName + ")'";
    }
}
