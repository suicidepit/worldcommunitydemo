package com.example.demo.domain.post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, UUID> {

  Optional<Post> findByIdAndDeletedFalse(UUID id);

  List<Post> findByDeletedFalseOrderByCreatedAtDesc();

  List<Post> findByCategoryIdAndDeletedFalseOrderByCreatedAtDesc(UUID categoryId);
}
