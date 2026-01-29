package com.example.demo.service;

import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.like.CommentLike;
import com.example.demo.domain.like.CommentLikeRepository;
import com.example.demo.domain.like.PostLike;
import com.example.demo.domain.like.PostLikeRepository;
import com.example.demo.domain.post.Post;
import com.example.demo.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

  private final PostLikeRepository postLikeRepository;
  private final CommentLikeRepository commentLikeRepository;

  public LikeService(PostLikeRepository postLikeRepository, CommentLikeRepository commentLikeRepository) {
    this.postLikeRepository = postLikeRepository;
    this.commentLikeRepository = commentLikeRepository;
  }

  @Transactional
  public boolean likePost(Post post, User user) {
    if (postLikeRepository.findByPostAndUser(post, user).isPresent()) {
      return false;
    }
    postLikeRepository.save(new PostLike(post, user));
    return true;
  }

  @Transactional
  public void unlikePost(Post post, User user) {
    postLikeRepository.findByPostAndUser(post, user)
        .ifPresent(postLikeRepository::delete);
  }

  @Transactional(readOnly = true)
  public long postLikeCount(Post post) {
    return postLikeRepository.countByPost(post);
  }

  @Transactional
  public boolean likeComment(Comment comment, User user) {
    if (commentLikeRepository.findByCommentAndUser(comment, user).isPresent()) {
      return false;
    }
    commentLikeRepository.save(new CommentLike(comment, user));
    return true;
  }

  @Transactional
  public void unlikeComment(Comment comment, User user) {
    commentLikeRepository.findByCommentAndUser(comment, user)
        .ifPresent(commentLikeRepository::delete);
  }

  @Transactional(readOnly = true)
  public long commentLikeCount(Comment comment) {
    return commentLikeRepository.countByComment(comment);
  }
}
