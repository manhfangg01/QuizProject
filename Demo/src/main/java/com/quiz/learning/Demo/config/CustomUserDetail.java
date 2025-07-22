package com.quiz.learning.Demo.config;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.quiz.learning.Demo.repository.UserRepository;

@Component("userDetailService")
public class CustomUserDetail implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== TRYING TO LOAD USER: " + username + " ===");

        Optional<com.quiz.learning.Demo.domain.User> user = this.userRepository.findByEmail(username);
        if (user.isEmpty()) {
            System.out.println("=== USER NOT FOUND IN DB ===");
            throw new UsernameNotFoundException("User not found");
        }
        com.quiz.learning.Demo.domain.User validUser = user.get();

        System.out.println("=== USER FOUND ===");
        System.out.println("Email: " + validUser.getEmail());
        System.out.println("Password: " + validUser.getPassword());
        System.out.println("CreatedAt: " + validUser.getCreatedAt());

        return new org.springframework.security.core.userdetails.User(
                validUser.getEmail(),
                validUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + validUser.getRole().getName())));
    }

    public CustomUserDetail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
