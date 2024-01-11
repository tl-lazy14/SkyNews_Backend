package com.example.skyNews.controller;

import com.example.skyNews.dto.request.ChangePasswordRequest;
import com.example.skyNews.dto.request.LoginRequest;
import com.example.skyNews.dto.request.RegisterRequest;
import com.example.skyNews.dto.response.AuthenticationResponse;
import com.example.skyNews.dto.response.ErrorResponse;
import com.example.skyNews.dto.response.VerificationCodeResponse;
import com.example.skyNews.entity.User;
import com.example.skyNews.service.AuthenticationService;
import com.example.skyNews.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Random;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User newUser = authenticationService.register(request);
            return ResponseEntity.ok().body(newUser);
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthenticationResponse authResponse = authenticationService.login(request, response);
        return ResponseEntity.ok().body(authResponse);
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authenticated!");
        }
        String newAccessToken = authenticationService.refreshAccessToken(refreshToken, response);
        return  ResponseEntity.ok().body(Collections.singletonMap("accessToken", newAccessToken));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            authenticationService.logout(request, response);
            return ResponseEntity.ok().body("Log out successfully!");
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PutMapping("/change-password/{user-id}")
    public ResponseEntity<?> changePassword(@PathVariable("user-id") Long id, @RequestBody ChangePasswordRequest request) {
        try {
            authenticationService.changePassword(id, request);
            return ResponseEntity.ok().body("Đổi mật khẩu thành công");
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder().error(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/generate-verification-code")
    public ResponseEntity<VerificationCodeResponse> generateVerificationCode(@RequestParam String email) {
        int codeLength = 6;
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder();

        for (int i = 0; i < codeLength; i++) {
            int digit = random.nextInt(10); // Số từ 0 đến 9
            codeBuilder.append(digit);
        }
        String verificationCode = codeBuilder.toString();

        emailService.sendVerificationCode(email, verificationCode);
        VerificationCodeResponse response = new VerificationCodeResponse();
        response.setCode(verificationCode);

        return ResponseEntity.ok().body(response);
    }
}
