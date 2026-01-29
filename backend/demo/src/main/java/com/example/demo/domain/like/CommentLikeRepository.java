package com.example.demo.domain.like;

import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {

  long countByComment(Comment comment);

  Optional<CommentLike> findByCommentAndUser(Comment comment, User user);
}
