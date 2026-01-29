package com.example.demo.service;

import com.example.demo.domain.category.Category;
import com.example.demo.domain.category.CategoryRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Transactional
  public Category createOrGet(String theme) {
    return categoryRepository.findByTheme(theme)
        .orElseGet(() -> categoryRepository.save(new Category(theme)));
  }

  @Transactional(readOnly = true)
  public Optional<Category> findByTheme(String theme) {
    return categoryRepository.findByTheme(theme);
  }

  @Transactional(readOnly = true)
  public Optional<Category> findById(UUID categoryId) {
    return categoryRepository.findById(categoryId);
  }
}
