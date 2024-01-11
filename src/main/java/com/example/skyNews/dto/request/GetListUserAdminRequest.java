package com.example.skyNews.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListUserAdminRequest {
    private String role;
    private String searchQuery;
    private int page;
    private int pageSize;
}
