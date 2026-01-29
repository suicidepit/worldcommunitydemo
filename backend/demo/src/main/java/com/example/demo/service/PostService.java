package com.example.demo.service;

import com.example.demo.domain.category.Category;
import com.example.demo.domain.post.Post;
import com.example.demo.domain.post.PostRepository;
import com.example.demo.domain.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Transactional
  public Post createPost(Category category, User author, String title, String content) {
    return postRepository.save(new Post(category, author, title, content));
  }

  @Transactional(readOnly = true)
  public Optional<Post> findActive(UUID postId) {
    return postRepository.findByIdAndDeletedFalse(postId);
  }

  @Transactional
  public Optional<Post> softDelete(UUID postId, UUID authorId) {
    return postRepository.findById(postId)
        .filter(post -> post.getAuthor()
            .getId()
            .equals(authorId))
        .map(post -> {
          post.softDelete();
          return post;
        });
  }

  @Transactional(readOnly = true)
  public List<Post> recentPosts() {
    return postRepository.findByDeletedFalseOrderByCreatedAtDesc();
  }

  @Transactional(readOnly = true)
  public List<Post> recentPostsByCategory(UUID categoryId) {
    return postRepository.findByCategoryIdAndDeletedFalseOrderByCreatedAtDesc(categoryId);
  }
}
