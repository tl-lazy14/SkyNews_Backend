package com.example.skyNews.serviceImpl;

import com.example.skyNews.dto.request.ChangeUsernameRequest;
import com.example.skyNews.dto.request.GetListUserAdminRequest;
import com.example.skyNews.dto.response.GetListUserAdminResponse;
import com.example.skyNews.dto.response.GetUserResponse;
import com.example.skyNews.entity.User;
import com.example.skyNews.repository.UserRepository;
import com.example.skyNews.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public GetUserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return GetUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getName())
                .role(user.getRole())
                .editorCategory(user.getEditorCategory())
                .build();
    }

    @Override
    public GetListUserAdminResponse getListUserAdmin(GetListUserAdminRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());
        List<User> listUserAdmin = userRepository.findByRoleAndNameContainingOrEmailContaining(
                request.getRole(),
                request.getSearchQuery(),
                pageable
        );
        int numUser = userRepository.countByRoleAndNameContainingOrEmailContaining(request.getRole(), request.getSearchQuery());
        return GetListUserAdminResponse.builder()
                .listUser(listUserAdmin)
                .numUser(numUser)
                .build();
    }

    @Override
    public void deleteUserAdmin(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changeUsername(Long id, ChangeUsernameRequest request) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(request.getNewUsername());
        userRepository.save(user);
    }
}
