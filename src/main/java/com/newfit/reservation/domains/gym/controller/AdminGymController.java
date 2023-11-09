package com.newfit.reservation.domains.gym.controller;

import com.newfit.reservation.domains.gym.domain.Gym;
import com.newfit.reservation.domains.gym.dto.request.admin.CreateGymRequest;
import com.newfit.reservation.domains.gym.dto.request.admin.UpdateGymRequest;
import com.newfit.reservation.domains.gym.service.AdminGymService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/admin/gyms")
public class AdminGymController {
    private final AdminGymService adminGymService;

    @GetMapping("/addGymForm")
    public String getAddGymForm(Model model) {
        model.addAttribute("gym", new CreateGymRequest());
        return "v1/admin/gyms/addGymForm";
    }

    @PostMapping("/addGymForm")
    public String createGym(@ModelAttribute CreateGymRequest request, Model model) {
        Gym gym = adminGymService.createGym(request);
        return "redirect:/v1/admin/gyms";
    }

    @GetMapping
    public String getAllGyms(Model model) {
        List<Gym> gyms = adminGymService.getAllGyms();
        model.addAttribute("gyms", gyms);
        return "v1/admin/gyms/index";
    }

    @GetMapping("/{gymId}")
    public String getGym(@PathVariable Long gymId, Model model) {
        Gym gym = adminGymService.getGymById(gymId);
        model.addAttribute("gym", gym);
        return "v1/admin/gyms/gym";
    }

    @GetMapping("/{gymId}/delete")
    public String deleteGym(@PathVariable Long gymId) {
        adminGymService.deleteById(gymId);
        return "redirect:/v1/admin/gyms";
    }

    @GetMapping("/{gymId}/edit")
    public String getEditForm(@PathVariable Long gymId, Model model) {
        Gym gym = adminGymService.getGymById(gymId);
        model.addAttribute("gym", UpdateGymRequest.from(gym));
        return "v1/admin/gyms/editGymForm";
    }

    @PostMapping("/{gymId}/edit")
    public String updateGym(@PathVariable Long gymId, @ModelAttribute UpdateGymRequest request) {
        adminGymService.updateGym(gymId, request);
        return "redirect:/v1/admin/gyms";
    }
}
