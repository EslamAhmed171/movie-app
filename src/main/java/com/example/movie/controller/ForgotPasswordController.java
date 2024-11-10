package com.example.movie.controller;

import com.example.movie.auth.Repository.ForgotPasswordRepository;
import com.example.movie.auth.Repository.UserRepository;
import com.example.movie.auth.entity.ForgotPassword;
import com.example.movie.auth.entity.User;
import com.example.movie.dto.ChangePassword;
import com.example.movie.dto.MailBody;
import com.example.movie.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserRepository userRepository,
                                    EmailService emailService,
                                    ForgotPasswordRepository forgotPasswordRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/verify-email/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email"));

        int OTP = OTPGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("OTP for forgot password")
                .text("This is the OTP for forgot password: " + OTP)
                .build();

        ForgotPassword forgotPassword = ForgotPassword.builder()
                .OTP(OTP)
                .expirationTime(new Date(System.currentTimeMillis() + 120 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMail(mailBody);
        forgotPasswordRepository.save(forgotPassword);
        return ResponseEntity.ok("Email send for verification");
    }

    @PostMapping("/verify-otp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable String otp, @PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please enter a valid email"));

        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(()-> new UsernameNotFoundException("Please enter a valid OTP"));
        if (forgotPassword.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.delete(forgotPassword);
            return new ResponseEntity<>("OTP is expired", HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok("OTP is valid");
    }

    @PostMapping("/change-password/{email}")
    public ResponseEntity<String> changePassword(@RequestBody ChangePassword changePassword,
                                                 @PathVariable String email) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatedPassword())) {
            return new ResponseEntity<>("Passwords don't match", HttpStatus.EXPECTATION_FAILED);
        }
        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encodedPassword);
        return ResponseEntity.ok("Password changed");
    }

    private Integer OTPGenerator () {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
