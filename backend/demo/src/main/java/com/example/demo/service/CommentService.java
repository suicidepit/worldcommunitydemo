package com.example.demo.service;

import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.comment.CommentRepository;
import com.example.demo.domain.post.Post;
import com.example.demo.domain.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

  private final CommentRepository commentRepository;

  public CommentService(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  @Transactional
  public Comment addComment(Post post, User author, Comment parent, String content) {
    return commentRepository.save(new Comment(post, author, parent, content));
  }

  @Transactional(readOnly = true)
  public Optional<Comment> findById(UUID commentId) {
    return commentRepository.findById(commentId);
  }

  @Transactional
  public Optional<Comment> softDelete(UUID commentId, UUID authorId) {
    return commentRepository.findById(commentId)
        .filter(comment -> comment.getAuthor()
            .getId()
            .equals(authorId))
        .map(comment -> {
          comment.softDelete();
          return comment;
        });
  }

  @Transactional(readOnly = true)
  public List<Comment> commentsForPost(Post post) {
    return commentRepository.findByPostOrderByCreatedAtAsc(post);
  }
}
