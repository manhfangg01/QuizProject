package com.quiz.learning.Demo.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Role;
import com.quiz.learning.Demo.repository.RoleRepository;

@Service
public class AdminRoleService {
    private final RoleRepository roleRepository;

    public AdminRoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> handleFetchAllRoles() {
        return this.roleRepository.findAll();
    }

}
