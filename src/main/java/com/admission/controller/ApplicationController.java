package com.admission.controller;

import com.admission.model.Application;
import com.admission.model.User;
import com.admission.repository.ApplicationRepository;
import com.admission.repository.CourseRepository;
import com.admission.repository.UserRepository;
import com.admission.service.ApplicationService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public ApplicationController(
            ApplicationService applicationService,
            ApplicationRepository applicationRepository,
            CourseRepository courseRepository,
            UserRepository userRepository) {

        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    // Open the admission application form
    @GetMapping("/new")
    public String showApplicationForm(Model model) {

        model.addAttribute("application", new Application());
        model.addAttribute("courses", courseRepository.findAll());

        return "applications/create";
    }

    // Display the logged-in user's applications
    @GetMapping({"", "/"})
    public String viewMyApplications(
            Authentication authentication,
            Model model,
            @RequestParam(
                    name = "submitted",
                    required = false) String submitted) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Logged-in user not found. Please log in again."
                        ));

        List<Application> applications =
                applicationService.getApplicationsByUser(user);

        model.addAttribute("applications", applications);
        model.addAttribute("submitted", submitted != null);

        return "applications/list";
    }

    // View details of one application belonging to the logged-in student
    @GetMapping("/{id}")
    public String viewApplicationDetails(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/login";
        }

        User loggedInUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found. Please log in again."
                        ));

        Application application = applicationRepository.findById(id)
                .orElse(null);

        // Do not allow a student to view another student's application
        if (application == null
                || application.getUser() == null
                || !application.getUser().getId()
                .equals(loggedInUser.getId())) {
            return "redirect:/applications";
        }

        model.addAttribute("application", application);

        return "applications/details";
    }

    // Submit a new application
    @PostMapping("/submit")
    public String submitApplication(
            @ModelAttribute("application") Application application,
            Authentication authentication,
            Model model) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/login";
        }

        try {
            User user = userRepository
                    .findByEmail(authentication.getName())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "User not found. Please log in again."
                            ));

            application.setUser(user);
            application.setEmail(user.getEmail());
            application.setPhone(user.getPhone());

            if (application.getFullName() == null
                    || application.getFullName().trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Please enter your full name."
                );
            }

            application.setFullName(application.getFullName().trim());

            applicationService.submitApplication(application);

            return "redirect:/applications?submitted=true";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("application", application);
            model.addAttribute("courses", courseRepository.findAll());

            return "applications/create";
        }
    }
}
