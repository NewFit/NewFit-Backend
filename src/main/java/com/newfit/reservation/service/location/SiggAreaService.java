package com.newfit.reservation.service.location;

import com.newfit.reservation.repository.location.SiggAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiggAreaService {
    private final SiggAreaRepository siggAreaRepository;

}
