package com.example.movie.auth.Repository;

import com.example.movie.auth.entity.ForgotPassword;
import com.example.movie.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.OTP = ?1 AND fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(String otp, User user);
}
