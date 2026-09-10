package com.gyandeep.library.controller;

import com.gyandeep.library.model.AdmissionStatus;
import com.gyandeep.library.model.User;
import com.gyandeep.library.service.AdmissionService;
import com.gyandeep.library.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdmissionService admissionService;
    private final UserService userService;

    public AdminController(AdmissionService admissionService, UserService userService) {
        this.admissionService = admissionService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalAdmissions", admissionService.countAll());
        model.addAttribute("pendingCount", admissionService.countByStatus(AdmissionStatus.PENDING));
        model.addAttribute("approvedCount", admissionService.countByStatus(AdmissionStatus.APPROVED));
        model.addAttribute("rejectedCount", admissionService.countByStatus(AdmissionStatus.REJECTED));
        model.addAttribute("totalUsers", userService.findAll().size());
        model.addAttribute("feePendingCount", admissionService.countFeePending());
        model.addAttribute("recentAdmissions", admissionService.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ---- Admission (library seat) access management ----
    @GetMapping("/admissions")
    public String listAdmissions(@RequestParam(required = false) String status, Model model) {
        if (status != null && !status.isBlank()) {
            model.addAttribute("admissions", admissionService.findByStatus(AdmissionStatus.valueOf(status)));
        } else {
            model.addAttribute("admissions", admissionService.findAll());
        }
        model.addAttribute("selectedStatus", status);
        return "admin/admissions";
    }

    @GetMapping("/admissions/{id}")
    public String viewAdmission(@PathVariable Long id, Model model) {
        model.addAttribute("admission", admissionService.findById(id));
        return "admin/admission-detail";
    }

    @PostMapping("/admissions/{id}/approve")
    public String approveAdmission(@PathVariable Long id,
                                    @RequestParam String seatNumber,
                                    @RequestParam String validTill,
                                    @RequestParam(required = false) String remarks) {
        admissionService.approve(id, seatNumber, LocalDate.parse(validTill), remarks);
        return "redirect:/admin/admissions/" + id;
    }

    @PostMapping("/admissions/{id}/reject")
    public String rejectAdmission(@PathVariable Long id, @RequestParam(required = false) String remarks) {
        admissionService.reject(id, remarks);
        return "redirect:/admin/admissions/" + id;
    }

    @PostMapping("/admissions/{id}/mark-fee-paid")
    public String markFeePaid(@PathVariable Long id,
                               @RequestParam(required = false) String receiptNumber,
                               @RequestParam(required = false) String collectedBy) {
        admissionService.markFeePaid(id, receiptNumber, collectedBy);
        return "redirect:/admin/admissions/" + id;
    }

    // ---- User account & access rights management ----
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/users/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        model.addAttribute("targetUser", userService.findAll().stream()
                .filter(u -> u.getId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        return "admin/user-detail";
    }

    @PostMapping("/users/{id}/access")
    public String updateAccess(@PathVariable Long id,
                                @RequestParam(defaultValue = "false") boolean canManageAdmissions,
                                @RequestParam(defaultValue = "false") boolean canManageBooks,
                                @RequestParam(defaultValue = "false") boolean canManageUsers,
                                @RequestParam(defaultValue = "false") boolean canViewReports,
                                @RequestParam(defaultValue = "false") boolean enabled) {
        userService.updateAccessRights(id, canManageAdmissions, canManageBooks, canManageUsers, canViewReports, enabled);
        return "redirect:/admin/users/" + id + "?updated=true";
    }
}
