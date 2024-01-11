package com.example.skyNews.controller;

import com.example.skyNews.dto.request.ChangeUsernameRequest;
import com.example.skyNews.dto.request.GetListUserAdminRequest;
import com.example.skyNews.dto.response.GetListUserAdminResponse;
import com.example.skyNews.dto.response.GetUserResponse;
import com.example.skyNews.entity.User;
import com.example.skyNews.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }
    @GetMapping("/admin/get-list-user")
    public ResponseEntity<GetListUserAdminResponse> getListUserAdmin(@RequestParam String role,
                                                                     @RequestParam String searchQuery,
                                                                     @RequestParam int page,
                                                                     @RequestParam int pageSize) {
        GetListUserAdminRequest request = new GetListUserAdminRequest(role, searchQuery, page, pageSize);
        GetListUserAdminResponse response = userService.getListUserAdmin(request);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/admin/delete/{user-id}")
    public void deleteUserAdmin(@PathVariable(value = "user-id") Long id) {
        userService.deleteUserAdmin(id);
    }
    @PutMapping("/change-username/{user-id}")
    public void changeUsername(@PathVariable(value = "user-id") Long userId,
                               @RequestBody ChangeUsernameRequest request) {
        userService.changeUsername(userId, request);
    }
}
