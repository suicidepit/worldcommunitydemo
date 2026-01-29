package com.example.demo.domain.like;

import com.example.demo.domain.post.Post;
import com.example.demo.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {

  long countByPost(Post post);

  Optional<PostLike> findByPostAndUser(Post post, User user);
}
