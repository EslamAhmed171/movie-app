package com.example.movie.auth.startup;

import com.example.movie.auth.Repository.UserRepository;
import com.example.movie.auth.entity.User;
import com.example.movie.auth.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class StartUp implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        var user = User
                .builder()
                .email("admin@gmail.com")
                .username("admin")
                .password(passwordEncoder.encode("12345"))
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(user);
    }
}
