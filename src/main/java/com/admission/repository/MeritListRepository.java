package com.admission.repository;

import com.admission.model.MeritList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeritListRepository extends JpaRepository<MeritList, Long> {

    List<MeritList> findByCourseIdOrderByRankAsc(Long courseId);

    List<MeritList> findByStatus(String status);
}