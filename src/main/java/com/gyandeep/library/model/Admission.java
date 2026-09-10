package com.gyandeep.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admissions")
public class Admission {

    public static final int ADMISSION_FEE = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---------- Personal ----------
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Gender is required")
    private String gender;

    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Enter a valid 10-digit phone number")
    private String phone;

    @NotBlank(message = "Email is required")
    private String email;

    // ---------- Address ----------
    @NotBlank(message = "Address is required")
    @Column(length = 500)
    private String address;

    private String village;
    private String taluka;
    private String district;
    private String pincode;

    // ---------- Education ----------
    private String qualification;
    private String college;

    // ---------- Exam & Membership ----------
    // Comma-separated list of selected exam categories, e.g. "MPSC,Police Bharti"
    @NotBlank(message = "Please select at least one exam category")
    @Column(length = 300)
    private String examCategory;

    @NotBlank(message = "Please select a membership type")
    private String membershipType;

    @NotBlank(message = "Please select a preferred shift")
    private String preferredShift;

    private String requiredFacility;

    // ---------- Emergency contact ----------
    private String parentName;
    private String parentMobile;

    // ---------- Library seat allocation (admin) ----------
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private AdmissionStatus status = AdmissionStatus.PENDING;

    private LocalDate admissionDate = LocalDate.now();
    private LocalDate membershipValidTill;

    @Column(length = 1000)
    private String adminRemarks;

    // ---------- Admission fee (cash payment) ----------
    private int admissionFee = ADMISSION_FEE;
    private String paymentMode = "Cash";
    private boolean feePaid = false;
    private String receiptNumber;
    private String collectedBy;
    private LocalDateTime feePaidAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public Admission() {}

    // ---------- Helpers ----------
    @Transient
    public List<String> getExamCategoryList() {
        List<String> list = new ArrayList<>();
        if (examCategory != null && !examCategory.isBlank()) {
            for (String s : examCategory.split(",")) {
                list.add(s.trim());
            }
        }
        return list;
    }

    // --- Getters / Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }

    public String getTaluka() { return taluka; }
    public void setTaluka(String taluka) { this.taluka = taluka; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getExamCategory() { return examCategory; }
    public void setExamCategory(String examCategory) { this.examCategory = examCategory; }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    public String getPreferredShift() { return preferredShift; }
    public void setPreferredShift(String preferredShift) { this.preferredShift = preferredShift; }

    public String getRequiredFacility() { return requiredFacility; }
    public void setRequiredFacility(String requiredFacility) { this.requiredFacility = requiredFacility; }

    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }

    public String getParentMobile() { return parentMobile; }
    public void setParentMobile(String parentMobile) { this.parentMobile = parentMobile; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public AdmissionStatus getStatus() { return status; }
    public void setStatus(AdmissionStatus status) { this.status = status; }

    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }

    public LocalDate getMembershipValidTill() { return membershipValidTill; }
    public void setMembershipValidTill(LocalDate membershipValidTill) { this.membershipValidTill = membershipValidTill; }

    public String getAdminRemarks() { return adminRemarks; }
    public void setAdminRemarks(String adminRemarks) { this.adminRemarks = adminRemarks; }

    public int getAdmissionFee() { return admissionFee; }
    public void setAdmissionFee(int admissionFee) { this.admissionFee = admissionFee; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public boolean isFeePaid() { return feePaid; }
    public void setFeePaid(boolean feePaid) { this.feePaid = feePaid; }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public String getCollectedBy() { return collectedBy; }
    public void setCollectedBy(String collectedBy) { this.collectedBy = collectedBy; }

    public LocalDateTime getFeePaidAt() { return feePaidAt; }
    public void setFeePaidAt(LocalDateTime feePaidAt) { this.feePaidAt = feePaidAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
