package com.quiz.learning.Demo.domain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String name;
    private String description;
    private Boolean isActive;

    // Mối quan hệ: Một vai trò có thể được gán cho nhiều user

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<User> users;
}
