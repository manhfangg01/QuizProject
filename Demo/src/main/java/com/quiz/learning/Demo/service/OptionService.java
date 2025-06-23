package com.quiz.learning.Demo.service;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.repository.OptionRepository;

@Service
public class OptionService {
    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

}
