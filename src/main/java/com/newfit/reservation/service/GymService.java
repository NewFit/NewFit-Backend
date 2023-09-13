package com.newfit.reservation.service;

import com.newfit.reservation.domain.Gym;
import com.newfit.reservation.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GymService {
    private final GymRepository gymRepository;

    public Long save(Gym gym) {
        return gymRepository.save(gym).getId();
    }

    public Optional<Gym> findById(Long gymId) {
        return gymRepository.findById(gymId);
    }
}
