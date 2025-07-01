package com.quiz.learning.Demo.domain;

import java.time.Instant;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private String email;
    private String password;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;
    private String UserAvatarUrls;

    @OneToMany(mappedBy = "user")
    private List<Result> results;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;

    @PrePersist
    public void handleBeforeCreating() {
        this.createdBy = "admin";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdating() {
        this.updatedBy = "admin";
        this.updatedAt = Instant.now();
    }
}
