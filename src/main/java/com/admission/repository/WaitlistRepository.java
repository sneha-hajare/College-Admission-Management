package com.admission.repository;

import com.admission.model.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {

    List<Waitlist> findByCourseIdOrderByRankAsc(Long courseId);

    List<Waitlist> findByApplicationId(Long applicationId);
}