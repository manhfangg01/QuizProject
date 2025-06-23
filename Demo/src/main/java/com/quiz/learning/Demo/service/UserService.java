package com.quiz.learning.Demo.service;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
