package com.newfit.reservation.domains.routine.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.newfit.reservation.domains.routine.domain.Routine;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoutineResponse {

    private final Long routineId;
    private final String name;

    /*
    이 생성자 내부에서 @RequiredArgsConstructor를 통해 생성되는 생성자를 호출하도록 작성했습니다.
    필드값을 직접 매개변수로 넘겨받는 것이 아니므로 우선은 Builder 패턴은 적용하지 않았습니다.
     */
    public RoutineResponse(Routine routine) {
       this(routine.getId(),routine.getName());
    }
}
