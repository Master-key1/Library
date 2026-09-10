package com.gyandeep.library.controller;

import com.gyandeep.library.model.Admission;
import com.gyandeep.library.model.User;
import com.gyandeep.library.service.AdmissionService;
import com.gyandeep.library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admission")
public class AdmissionController {

    // Library's fixed address, pre-filled as defaults on the form
    public static final String LIB_VILLAGE = "Nandurga";
    public static final String LIB_TALUKA = "Ausa";
    public static final String LIB_DISTRICT = "Latur";
    public static final String LIB_PINCODE = "413520";
    public static final String LIB_ADDRESS_LINE = "At Post Nandurga, Ta. Ausa, Dist. Latur";

    private final AdmissionService admissionService;
    private final UserService userService;

    public AdmissionController(AdmissionService admissionService, UserService userService) {
        this.admissionService = admissionService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showForm(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        if (user.getAdmission() != null) {
            return "redirect:/profile";
        }
        Admission admission = new Admission();
        admission.setVillage(LIB_VILLAGE);
        admission.setTaluka(LIB_TALUKA);
        admission.setDistrict(LIB_DISTRICT);
        admission.setPincode(LIB_PINCODE);
        model.addAttribute("admission", admission);
        model.addAttribute("libAddressLine", LIB_ADDRESS_LINE);
        return "admission-form";
    }

    @PostMapping("/new")
    public String submitForm(@Valid @ModelAttribute("admission") Admission admission,
                              BindingResult bindingResult,
                              @RequestParam(name = "examCategories", required = false) List<String> examCategories,
                              @RequestParam(name = "feeAcknowledge", required = false) Boolean feeAcknowledge,
                              Authentication authentication,
                              Model model) {

        // Combine the checkbox list into the stored comma-separated field
        if (examCategories != null && !examCategories.isEmpty()) {
            admission.setExamCategory(String.join(",", examCategories));
        } else {
            admission.setExamCategory("");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("libAddressLine", LIB_ADDRESS_LINE);
            return "admission-form";
        }

        if (examCategories == null || examCategories.isEmpty()) {
            model.addAttribute("examError", "Please select at least one exam category.");
            model.addAttribute("libAddressLine", LIB_ADDRESS_LINE);
            return "admission-form";
        }

        if (feeAcknowledge == null || !feeAcknowledge) {
            model.addAttribute("feeError", "Please acknowledge the ₹200 admission fee (payable in cash) before submitting.");
            model.addAttribute("libAddressLine", LIB_ADDRESS_LINE);
            return "admission-form";
        }

        User user = userService.findByUsername(authentication.getName());
        Admission saved = admissionService.submit(admission, user);
        model.addAttribute("admission", saved);
        return "admission-success";
    }
}
