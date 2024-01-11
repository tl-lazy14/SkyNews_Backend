package com.example.skyNews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetListCategoryTopicResponse {
    private List<CategoryResponse> listCategory;
    private int numCategory;
}
