package com.newfit.reservation.service;

import com.newfit.reservation.repository.SidoAreaRepository;
import com.newfit.reservation.repository.SiggAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SidoAreaService {
    private final SidoAreaRepository sidoAreaRepository;
    
}
