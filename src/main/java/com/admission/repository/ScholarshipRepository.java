package com.admission.repository;

import com.admission.model.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {

    List<Scholarship> findByDeadlineIsNotNull();

    List<Scholarship> findByNameContainingIgnoreCase(String name);
}