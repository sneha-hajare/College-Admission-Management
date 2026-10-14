package com.admission.controller;

import com.admission.model.Application;
import com.admission.service.ApplicationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ApplicationService applicationService;

    public AdminController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Display admin dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<Application> applications =
                applicationService.getAllApplications();

        model.addAttribute("applications", applications);
        model.addAttribute("totalApplications", applications.size());

        model.addAttribute("pendingApplications",
                applications.stream()
                        .filter(a -> "PENDING".equalsIgnoreCase(a.getStatus()))
                        .count());

        model.addAttribute("approvedApplications",
                applications.stream()
                        .filter(a -> "APPROVED".equalsIgnoreCase(a.getStatus()))
                        .count());

        model.addAttribute("rejectedApplications",
                applications.stream()
                        .filter(a -> "REJECTED".equalsIgnoreCase(a.getStatus()))
                        .count());

        return "admin/dashboard";
    }

    // Approve an application
    @PostMapping("/applications/{id}/approve")
    public String approveApplication(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            applicationService.updateApplicationStatus(id, "APPROVED");
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Application approved successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    // Reject an application
    @PostMapping("/applications/{id}/reject")
    public String rejectApplication(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            applicationService.updateApplicationStatus(id, "REJECTED");
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Application rejected successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }
}
