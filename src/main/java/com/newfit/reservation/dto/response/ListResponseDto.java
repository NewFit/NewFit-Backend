package com.newfit.reservation.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ListResponseDto<T> {
    private int count;
    private List<T> data;

    @Builder
    public ListResponseDto(List<T> data) {
        this.data = data;
        this.count = data.size();
    }

}
