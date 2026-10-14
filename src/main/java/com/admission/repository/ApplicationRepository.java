
package com.admission.repository;

import com.admission.model.Application;
import com.admission.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByUser(User user);
}