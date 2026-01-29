package com.example.demo.web.controller;

import com.example.demo.domain.category.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.web.dto.CategoryDtos;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryDtos.CategoryResponse createCategory(
      @Valid @RequestBody CategoryDtos.CreateCategoryRequest request
  ) {
    Category category = categoryService.createOrGet(request.theme());
    return CategoryDtos.CategoryResponse.from(category);
  }
}
