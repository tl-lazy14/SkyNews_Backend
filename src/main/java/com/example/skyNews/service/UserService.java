package com.example.skyNews.service;

import com.example.skyNews.dto.response.GetUserByIdResponse;

public interface UserService {
    GetUserByIdResponse getUserById(Long id);
}
