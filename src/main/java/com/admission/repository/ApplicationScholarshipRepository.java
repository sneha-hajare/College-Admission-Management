package com.admission.repository;

import com.admission.model.ApplicationScholarship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationScholarshipRepository
        extends JpaRepository<ApplicationScholarship, Long> {

    List<ApplicationScholarship> findByApplicationId(Long applicationId);

    List<ApplicationScholarship> findByScholarshipId(Long scholarshipId);

    List<ApplicationScholarship> findByStatus(String status);
}