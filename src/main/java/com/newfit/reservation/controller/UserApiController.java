package com.newfit.reservation.controller;


import com.newfit.reservation.dto.request.routine.UserUpdateRequest;
import com.newfit.reservation.dto.response.UserSimpleResponse;
import com.newfit.reservation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {

    private final UserService userService;

    @PatchMapping
    public UserSimpleResponse modify(@Valid @RequestBody UserUpdateRequest request){

        Long userId = 4L;

        return userService.modify(userId, request);
    }
}
