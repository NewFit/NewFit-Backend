package com.newfit.reservation.dto.response;

import com.newfit.reservation.domain.routine.Routine;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineResponse {

    private final Long id;
    private final String name;

    /*
    이 생성자 내부에서 @RequiredArgsConstructor를 통해 생성되는 생성자를 호출하도록 작성했습니다.
    필드값을 직접 매개변수로 넘겨받는 것이 아니므로 우선은 Builder 패턴은 적용하지 않았습니다.
     */
    public RoutineResponse(Routine routine) {
       this(routine.getId(),routine.getName());
    }
}