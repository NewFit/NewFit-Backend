package com.newfit.reservation.controller;

import com.newfit.reservation.dto.request.GymRequestDto;
import com.newfit.reservation.dto.response.GymResponseDto;
import com.newfit.reservation.dto.response.ListResponseDto;
import com.newfit.reservation.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@ResponseStatus(HttpStatus.OK)
@RestController
@RequestMapping("/api/v1/gyms/authority")
@RequiredArgsConstructor
public class GymApiController {
    private final AuthorityService authorityService;

    @PostMapping
    public Long register(@RequestHeader(value = "User-Id") Long userId,
                         @RequestBody GymRequestDto request) {
        return authorityService.register(userId, request.getGymId());
    }

    @GetMapping
    public ListResponseDto<GymResponseDto> listRegistration(@RequestHeader(value = "User-Id") Long id) {
        return authorityService.listRegistration(id);
    }

    @DeleteMapping
    public void delete(@RequestHeader(value = "User-Id") Long userId,
                       @RequestBody GymRequestDto request) {
        authorityService.delete(userId, request.getGymId());
    }
}
