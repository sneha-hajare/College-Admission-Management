package com.admission.repository;

import com.admission.model.ApplicationNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationNoteRepository
        extends JpaRepository<ApplicationNote, Long> {

    List<ApplicationNote> findByApplicationId(Long applicationId);

    List<ApplicationNote> findByAdminId(Long adminId);
}