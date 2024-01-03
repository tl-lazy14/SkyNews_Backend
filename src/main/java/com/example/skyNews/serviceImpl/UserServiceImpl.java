package com.example.skyNews.serviceImpl;

import com.example.skyNews.dto.response.GetUserByIdResponse;
import com.example.skyNews.entity.User;
import com.example.skyNews.repository.UserRepository;
import com.example.skyNews.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public GetUserByIdResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return GetUserByIdResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getName())
                .role(user.getRole())
                .editorCategory(user.getEditorCategory())
                .build();
    }
}
