package com.newfit.reservation.controller;


import com.newfit.reservation.dto.request.UserUpdateRequest;
import com.newfit.reservation.dto.response.UserDetailResponse;
import com.newfit.reservation.dto.response.UserSimpleResponse;
import com.newfit.reservation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {

    private final UserService userService;

    @PatchMapping
    public UserSimpleResponse modify(@Valid @RequestBody UserUpdateRequest request) {

        // TODO: remove this userId and apply security
        Long userId = 4L;

        return userService.modify(userId, request);
    }

    @GetMapping
    public UserDetailResponse userDetail() {

        // TODO: remove this userId and apply security
        Long authorityId = 4L;

        return userService.userDetail(authorityId);
    }

    @DeleteMapping
    public UserSimpleResponse drop() {
        // TODO: remove this userId and apply security
        Long userId = 4L;

        return userService.drop(userId);
    }
}
