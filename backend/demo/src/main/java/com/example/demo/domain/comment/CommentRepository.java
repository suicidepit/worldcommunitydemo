package com.example.demo.domain.comment;

import com.example.demo.domain.post.Post;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

  List<Comment> findByPostOrderByCreatedAtAsc(Post post);
}
