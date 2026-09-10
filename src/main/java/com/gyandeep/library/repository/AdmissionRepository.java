package com.gyandeep.library.repository;

import com.gyandeep.library.model.Admission;
import com.gyandeep.library.model.AdmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    List<Admission> findByStatus(AdmissionStatus status);
    List<Admission> findAllByOrderByCreatedAtDesc();
}
