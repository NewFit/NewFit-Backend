package com.newfit.reservation.service.location;

import com.newfit.reservation.repository.location.EmdAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmdAreaService {
    private final EmdAreaRepository emdAreaRepository;
    
}
