package com.newfit.reservation.domains.authority.controller;

import com.newfit.reservation.common.auth.AuthorityCheckService;
import com.newfit.reservation.domains.authority.dto.request.manager.*;
import com.newfit.reservation.domains.authority.dto.response.EquipmentGymListResponse;
import com.newfit.reservation.domains.authority.dto.response.manager.UserAndPendingListResponse;
import com.newfit.reservation.domains.authority.service.AuthorityService;
import com.newfit.reservation.domains.equipment.domain.Equipment;
import com.newfit.reservation.domains.equipment.service.EquipmentGymService;
import com.newfit.reservation.domains.equipment.service.EquipmentService;
import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.gym.service.GymService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerApiController {
    private final EquipmentService equipmentService;
    private final AuthorityService authorityService;
    private final EquipmentGymService equipmentGymService;
    private final GymService gymService;
    private final AuthorityCheckService authorityCheckService;

    /*
    user의 gym을 조회
    gym에 equipment 등록
    equipmentGym에 count만큼 등록
     */
    @PostMapping("/equipments")
    public ResponseEntity<Void> registerEquipment(Authentication authentication,
                                                  @RequestHeader(value = "authority-id") Long authorityId,
                                                  @Valid @RequestBody RegisterEquipmentRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        Gym gym = authorityService.getGymByAuthorityId(authorityId);
        Equipment equipment = equipmentService.registerEquipment(gym, request.getName(), request.getPurposeType());
        equipmentGymService.registerEquipmentInGym(equipment, gym, request.getCount(), request.getEquipmentGymNames());

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    /*
    모든 Gym을 조회하여 반환
     */
    @GetMapping("/equipment-gym")
    public ResponseEntity<EquipmentGymListResponse> getAllEquipment(@RequestHeader(name = "authority-id") Long authorityId) {
        Gym gym = authorityService.getGymByAuthorityId(authorityId);
        EquipmentGymListResponse allInGym = authorityService.findAllInGym(gym);

        return ResponseEntity.ok(allInGym);
    }

    /*
    condition 수정
     */
    @PatchMapping("/equipment-gym/{equipmentGymId}")
    public ResponseEntity<Void> updateEquipmentCondition(Authentication authentication,
                                                         @RequestHeader("authority-id") Long authorityId,
                                                         @PathVariable Long equipmentGymId,
                                                         @Valid @RequestBody UpdateConditionRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        equipmentGymService.updateCondition(equipmentGymId, request.getConditionType());
        return ResponseEntity
                .noContent()
                .build();
    }

    /*
    equipment를 삭제
    cascade 설정에 따라 연관된 EquipmentGym도 삭제
     */
    @DeleteMapping("/equipments")
    public ResponseEntity<Void> deleteEquipment( Authentication authentication,
                                                 @RequestHeader("authority-id") Long authorityId,
                                                 @Valid @RequestBody DeleteEquipmentRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        equipmentService.deleteEquipment(request.getEquipmentId());
        return ResponseEntity
                .noContent()
                .build();
    }

    /*
    특정 EquipmentGym 삭제
     */
    @DeleteMapping("/equipment-gym")
    public ResponseEntity<Void> deleteEquipmentGym(Authentication authentication,
                                                   @RequestHeader("authority-id") Long authorityId,
                                                   @Valid @RequestBody DeleteEquipmentGymRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        equipmentGymService.deleteEquipmentGym(request.getEquipmentGymId());
        return ResponseEntity
                .noContent()
                .build();
    }

    /*
    userId와 gymId를 받아서 해당 유저를 승인하는 메소드입니다.
    AuthorityService에게 로직 실행을 위임한 뒤에 Dto를 넘겨받아 반환합니다.
     */
    @PostMapping("/authority")
    public ResponseEntity<Void> acceptUser(Authentication authentication,
                                           @RequestHeader("authority-id") Long authorityId,
                                           @Valid @RequestBody UserAcceptRequest request) {
        authorityCheckService.validateByAuthorityId(authentication, authorityId);
        authorityService.acceptUser(request.getUserId(), request.getGymId());
        return ResponseEntity
                .status(CREATED)
                .build();
    }

    /*
    특정 Gym을 이용하는 유저 및 승인 요청을 보낸 유저를 리스트 형식으로 반환하는 메소드입니다.
    마찬가지로 AuthorityService에게 로직 실행을 위임한 뒤에 Dto를 넘겨받아 반환합니다.
     */
    @GetMapping("/authority")
    public ResponseEntity<UserAndPendingListResponse> getUserAndAcceptRequestList(@RequestHeader("authority-id") Long authorityId) {
        UserAndPendingListResponse responseDto = authorityService.getUserAndAcceptRequestList(authorityId);

        return ResponseEntity.ok(responseDto);
    }
}
