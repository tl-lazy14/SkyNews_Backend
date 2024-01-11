package com.example.skyNews.dto.response;

import com.example.skyNews.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetListUserAdminResponse {
    private List<User> listUser;
    private int numUser;
}
