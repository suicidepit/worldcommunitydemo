package com.example.demo.web.dto;

import com.example.demo.domain.category.Category;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

public final class CategoryDtos {

  private CategoryDtos() {
  }

  public record CreateCategoryRequest(
      @NotBlank String theme
  ) {
  }

  public record CategoryResponse(
      UUID id,
      String theme,
      Instant createdAt
  ) {
    public static CategoryResponse from(Category category) {
      return new CategoryResponse(category.getId(), category.getTheme(), category.getCreatedAt());
    }
  }
}
