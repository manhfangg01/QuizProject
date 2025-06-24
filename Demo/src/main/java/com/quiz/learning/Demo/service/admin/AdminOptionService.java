package com.quiz.learning.Demo.service.admin;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.repository.OptionRepository;

@Service
public class AdminOptionService {
    private final OptionRepository optionRepository;

    public AdminOptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

}
