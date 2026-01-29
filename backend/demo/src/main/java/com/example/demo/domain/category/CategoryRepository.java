package com.example.demo.domain.category;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

  Optional<Category> findByTheme(String theme);
}
