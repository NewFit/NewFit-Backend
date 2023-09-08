package com.newfit.reservation.service.location;

import com.newfit.reservation.repository.location.SidoAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SidoAreaService {
    private final SidoAreaRepository sidoAreaRepository;
    
}
