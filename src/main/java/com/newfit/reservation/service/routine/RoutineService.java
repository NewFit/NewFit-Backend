package com.newfit.reservation.service.routine;


import com.newfit.reservation.domain.User;
import com.newfit.reservation.domain.routine.Routine;
import com.newfit.reservation.repository.routine.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineService {

    private final RoutineRepository routineRepository;

    /*
    이전에 등록한 루틴과 동일한 이름이라면 exception이 발생합니다.
    아니라면 새로운 Routine을 등록하고 그 Routine을 반환합니다.
     */
    public Routine registerRoutine(User user, String routineName) {
        if(validateDuplicate(user, routineName))
            throw new IllegalArgumentException();

        return routineRepository.save(Routine.builder()
                .user(user)
                .name(routineName)
                .build());
    }

    // 해당 유저가 이전에 등록한 Routine중에 동일한 이름이 있는지 확인합니다.
    private boolean validateDuplicate(User user, String routineName) {
        return routineRepository.findByUserAndName(user, routineName).isPresent();
    }
}
