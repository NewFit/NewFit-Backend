package com.newfit.reservation.domains.authority.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.newfit.reservation.domains.authority.dto.request.admin.AssignManagerRequest;
import com.newfit.reservation.domains.authority.service.AdminManagerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/admin/managers")
public class AdminManagerController {
	private final AdminManagerService adminManagerService;

	@GetMapping("/addManagerForm")
	public String getAddManagerForm(Model model) {
		model.addAttribute("form", new AssignManagerRequest());
		return "v1/admin/managers/addManagerForm";
	}

	@PostMapping("/addManagerForm")
	public String createGym(@ModelAttribute AssignManagerRequest request) {
		adminManagerService.assignManager(request);
		return "redirect:/v1/admin";
	}
}
