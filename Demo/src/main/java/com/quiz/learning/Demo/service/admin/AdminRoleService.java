package com.quiz.learning.Demo.service.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Role;
import com.quiz.learning.Demo.repository.RoleRepository;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminRoleService {
    private final RoleRepository roleRepository;

    public AdminRoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> handleFetchAllRoles() {
        return this.roleRepository.findAll();
    }

    public Role handleFetchRoleById(Long id) {
        Optional<Role> checkRole = this.roleRepository.findById(id);
        if (checkRole.isEmpty()) {
            throw new ObjectNotFound("Role not found");
        }
        return checkRole.get();
    }

}
