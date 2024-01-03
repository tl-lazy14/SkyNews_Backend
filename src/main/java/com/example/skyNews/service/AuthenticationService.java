package com.example.skyNews.service;

import com.example.skyNews.dto.request.ChangePasswordRequest;
import com.example.skyNews.dto.request.LoginRequest;
import com.example.skyNews.dto.request.RegisterRequest;
import com.example.skyNews.dto.response.AuthenticationResponse;
import com.example.skyNews.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    User register(RegisterRequest request);
    AuthenticationResponse login(LoginRequest request, HttpServletResponse httpServletResponse);
    String refreshAccessToken(String refreshToken, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
    void changePassword(Long id, ChangePasswordRequest request);
}
