package com.admission.service;

import com.admission.model.Application;
import com.admission.model.Course;
import com.admission.model.User;
import com.admission.repository.ApplicationRepository;
import com.admission.repository.CourseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CourseRepository courseRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            CourseRepository courseRepository) {

        this.applicationRepository = applicationRepository;
        this.courseRepository = courseRepository;
    }

    // Submit a new student application
    @Transactional
    public Application submitApplication(Application application) {

        if (application.getFullName() == null
                || application.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }

        if (application.getUser() == null) {
            throw new IllegalArgumentException(
                    "Please log in before submitting your application.");
        }

        if (application.getCourse() == null
                || application.getCourse().getId() == null) {
            throw new IllegalArgumentException("Please select a course.");
        }

        Long courseId = application.getCourse().getId();

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Selected course was not found."));

        application.setFullName(application.getFullName().trim());
        application.setCourse(course);
        application.setStatus("PENDING");

        return applicationRepository.save(application);
    }

    // Get applications belonging to a particular student
    @Transactional(readOnly = true)
    public List<Application> getApplicationsByUser(User user) {
        return applicationRepository.findByUser(user);
    }

    // Get all student applications for the admin dashboard
    @Transactional(readOnly = true)
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    // Approve or reject a student application
    @Transactional
    public Application updateApplicationStatus(
            Long applicationId, String status) {

        if (status == null
                || (!"APPROVED".equalsIgnoreCase(status)
                && !"REJECTED".equalsIgnoreCase(status))) {
            throw new IllegalArgumentException(
                    "Status must be APPROVED or REJECTED.");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Application not found."));

        application.setStatus(status.toUpperCase());

        return applicationRepository.save(application);
    }
}
