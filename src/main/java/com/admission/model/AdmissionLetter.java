package com.admission.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admission_letters")
public class AdmissionLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    @Column(name = "letter_url")
    private String letterUrl;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    public AdmissionLetter() {
    }

    @PrePersist
    public void prePersist() {
        if (generatedAt == null) {
            generatedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getLetterUrl() {
        return letterUrl;
    }

    public void setLetterUrl(String letterUrl) {
        this.letterUrl = letterUrl;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}