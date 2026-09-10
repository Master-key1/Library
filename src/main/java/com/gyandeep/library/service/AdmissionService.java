package com.gyandeep.library.service;

import com.gyandeep.library.model.Admission;
import com.gyandeep.library.model.AdmissionStatus;
import com.gyandeep.library.model.User;
import com.gyandeep.library.repository.AdmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdmissionService {

    private final AdmissionRepository admissionRepository;

    public AdmissionService(AdmissionRepository admissionRepository) {
        this.admissionRepository = admissionRepository;
    }

    @Transactional
    public Admission submit(Admission admission, User user) {
        admission.setUser(user);
        admission.setStatus(AdmissionStatus.PENDING);
        return admissionRepository.save(admission);
    }

    public List<Admission> findAll() {
        return admissionRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Admission> findByStatus(AdmissionStatus status) {
        return admissionRepository.findByStatus(status);
    }

    public Admission findById(Long id) {
        return admissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admission not found"));
    }

    @Transactional
    public void approve(Long id, String seatNumber, LocalDate validTill, String remarks) {
        Admission admission = findById(id);
        admission.setStatus(AdmissionStatus.APPROVED);
        admission.setSeatNumber(seatNumber);
        admission.setMembershipValidTill(validTill);
        admission.setAdminRemarks(remarks);
        admissionRepository.save(admission);
    }

    @Transactional
    public void reject(Long id, String remarks) {
        Admission admission = findById(id);
        admission.setStatus(AdmissionStatus.REJECTED);
        admission.setAdminRemarks(remarks);
        admissionRepository.save(admission);
    }

    public long countByStatus(AdmissionStatus status) {
        return admissionRepository.findByStatus(status).size();
    }

    public long countAll() {
        return admissionRepository.count();
    }

    @Transactional
    public void markFeePaid(Long id, String receiptNumber, String collectedBy) {
        Admission admission = findById(id);
        admission.setFeePaid(true);
        admission.setReceiptNumber(receiptNumber);
        admission.setCollectedBy(collectedBy);
        admission.setFeePaidAt(java.time.LocalDateTime.now());
        admissionRepository.save(admission);
    }

    public long countFeePending() {
        return admissionRepository.findAll().stream().filter(a -> !a.isFeePaid()).count();
    }
}
