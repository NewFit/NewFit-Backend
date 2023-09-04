package com.newfit.reservation.service;

import com.newfit.reservation.repository.EmdAreaRepository;
import com.newfit.reservation.repository.SiggAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmdAreaService {
    private final EmdAreaRepository emdAreaRepository;
    
}
