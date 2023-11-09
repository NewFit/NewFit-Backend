package com.newfit.reservation.domains.gym.service;

import com.newfit.reservation.common.exception.CustomException;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.gym.dto.request.admin.CreateGymRequest;
import com.newfit.reservation.domains.gym.dto.request.admin.UpdateGymRequest;
import com.newfit.reservation.domains.gym.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.newfit.reservation.common.exception.ErrorCodeType.GYM_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminGymService {
    private final GymRepository gymRepository;

    public Gym createGym(CreateGymRequest request) {
        return gymRepository.save(Gym.from(request));
    }

    public List<Gym> getAllGyms() {
        return gymRepository.findAll();
    }

    public Gym getGymById(Long gymId) {
        return findGymById(gymId);
    }

    public void deleteById(Long gymId) {
        gymRepository.deleteById(gymId);
    }

    public void updateGym(Long gymId, UpdateGymRequest request) {
        Gym gym = findGymById(gymId);

        gym.updateName(request.getName());
        gym.updateTel(request.getTel());
        gym.updateAddress(request.getAddress());
        gym.updateBusinessTime(request.getOpenAt(), request.getCloseAt(), request.getAllDay());
    }

    private Gym findGymById(Long gymId) {
        return gymRepository.findById(gymId)
                .orElseThrow(() -> new CustomException(GYM_NOT_FOUND));
    }
}
