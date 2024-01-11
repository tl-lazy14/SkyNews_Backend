package com.example.skyNews.service;

import com.example.skyNews.dto.request.ChangeUsernameRequest;
import com.example.skyNews.dto.request.GetListUserAdminRequest;
import com.example.skyNews.dto.response.GetListUserAdminResponse;
import com.example.skyNews.dto.response.GetUserResponse;
import com.example.skyNews.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    GetUserResponse getUserById(Long id);
    GetListUserAdminResponse getListUserAdmin(GetListUserAdminRequest request);
    void deleteUserAdmin(Long id);
    void changeUsername(Long id, ChangeUsernameRequest request);
}
