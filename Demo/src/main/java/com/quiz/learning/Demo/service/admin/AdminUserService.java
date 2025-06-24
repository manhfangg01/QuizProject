package com.quiz.learning.Demo.service.admin;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.repository.UserRepository;

@Service
public class AdminUserService {
    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
