package com.example.skyNews.serviceImpl;

import com.example.skyNews.dto.request.ChangePasswordRequest;
import com.example.skyNews.dto.request.LoginRequest;
import com.example.skyNews.dto.request.RegisterRequest;
import com.example.skyNews.dto.response.AuthenticationResponse;
import com.example.skyNews.entity.User;
import com.example.skyNews.repository.UserRepository;
import com.example.skyNews.service.AuthenticationService;
import com.example.skyNews.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private static List<String> refreshTokens = new ArrayList<>();
    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());
        newUser.setEditorCategory(request.getEditorCategory());
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public AuthenticationResponse login(LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmailAndRole(request.getEmail(), request.getRole())
                .orElseThrow();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshTokens.add(refreshToken);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(false);
        response.addCookie(refreshTokenCookie);
        return AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .editorCategory(user.getEditorCategory())
                .accessToken(accessToken)
                .build();
    }
    @Override
    public String refreshAccessToken(String refreshToken, HttpServletResponse response) {
        if (!refreshTokens.contains(refreshToken)) {
            throw new RuntimeException("Refresh token is not valid!");
        }

        String userEmail = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            refreshTokens = refreshTokens.stream()
                    .filter(token -> !token.equals(refreshToken))
                    .collect(Collectors.toList());
            String newAccessToken = jwtService.generateAccessToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            refreshTokens.add(newRefreshToken);
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setSecure(false);
            response.addCookie(refreshTokenCookie);
            return newAccessToken;
        } else {
            throw new RuntimeException("Refresh token is not valid!");
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    String refreshToken = cookie.getValue();
                    refreshTokens = refreshTokens.stream()
                            .filter(token -> !token.equals(refreshToken))
                            .collect(Collectors.toList());
                }
            }
        }
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequest request) {
        User user = userRepository.findById(id).orElseThrow();
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }
        String newPassword = request.getNewPassword();
        String hashedNewPassword = bCryptPasswordEncoder.encode(newPassword);

        user.setPassword(hashedNewPassword);
        userRepository.save(user);
    }
}
