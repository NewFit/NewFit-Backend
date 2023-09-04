package com.newfit.reservation.service;

import com.newfit.reservation.repository.SiggAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiggAreaService {
    private final SiggAreaRepository siggAreaRepository;

}
