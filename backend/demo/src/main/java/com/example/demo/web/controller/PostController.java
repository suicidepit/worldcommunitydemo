package com.example.demo.web.controller;

import com.example.demo.domain.category.Category;
import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.post.Post;
import com.example.demo.domain.user.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CommentService;
import com.example.demo.service.LikeService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;
import com.example.demo.web.dto.CommentDtos;
import com.example.demo.web.dto.LikeDtos;
import com.example.demo.web.dto.PostDtos;
import com.example.demo.web.dto.WorldIdDtos.WorldIdAction;
import com.example.demo.web.dto.WorldIdDtos.WorldIdProofPayload;
import com.example.demo.worldid.WorldIdVerificationService;
import com.example.demo.worldid.WorldIdVerificationService.WorldIdVerificationException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final PostService postService;
  private final CategoryService categoryService;
  private final UserService userService;
  private final CommentService commentService;
  private final LikeService likeService;
  private final WorldIdVerificationService worldIdVerificationService;

  public PostController(
      PostService postService,
      CategoryService categoryService,
      UserService userService,
      CommentService commentService,
      LikeService likeService,
      WorldIdVerificationService worldIdVerificationService
  ) {
    this.postService = postService;
    this.categoryService = categoryService;
    this.userService = userService;
    this.commentService = commentService;
    this.likeService = likeService;
    this.worldIdVerificationService = worldIdVerificationService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PostDtos.PostResponse createPost(@Valid @RequestBody PostDtos.CreatePostRequest request) {
    User author = userService.findByWalletAddress(request.authorWallet())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    Category category = categoryService.findById(request.categoryId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

    verifyWorldIdProof(WorldIdAction.POST_CREATE, request.proof(), author);
    Post post = postService.createPost(category, author, request.title(), request.content());
    return PostDtos.PostResponse.from(post);
  }

  @GetMapping
  public List<PostDtos.PostResponse> listPosts(@RequestParam(required = false) UUID categoryId) {
    List<Post> posts = categoryId == null
        ? postService.recentPosts()
        : postService.recentPostsByCategory(categoryId);
    return posts.stream()
        .map(PostDtos.PostResponse::from)
        .collect(Collectors.toList());
  }

  @GetMapping("/{postId}")
  public PostDtos.PostDetailResponse getPost(@PathVariable UUID postId) {
    Post post = postService.findActive(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    List<PostDtos.CommentResponse> comments = commentService.commentsForPost(post).stream()
        .map(PostDtos.CommentResponse::from)
        .collect(Collectors.toList());
    return new PostDtos.PostDetailResponse(PostDtos.PostResponse.from(post), comments);
  }

  @PostMapping("/{postId}/comments")
  @ResponseStatus(HttpStatus.CREATED)
  public PostDtos.CommentResponse addComment(
      @PathVariable UUID postId,
      @Valid @RequestBody CommentDtos.CreateCommentRequest request
  ) {
    Post post = postService.findActive(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    User author = userService.findByWalletAddress(request.authorWallet())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

    Comment parent = null;
    if (request.parentCommentId() != null) {
      parent = commentService.findById(request.parentCommentId())
          .filter(existing -> existing.getPost().getId().equals(post.getId()))
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent comment invalid"));
    }

    verifyWorldIdProof(WorldIdAction.COMMENT_CREATE, request.proof(), author);
    Comment comment = commentService.addComment(post, author, parent, request.content());
    return PostDtos.CommentResponse.from(comment);
  }

  @DeleteMapping("/{postId}")
  public PostDtos.PostResponse deletePost(
      @PathVariable UUID postId,
      @RequestParam("wallet") String walletAddress
  ) {
    User author = userService.findByWalletAddress(walletAddress)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    return postService.softDelete(postId, author.getId())
        .map(PostDtos.PostResponse::from)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found or not owned"));
  }

  @PostMapping("/{postId}/likes")
  public LikeResponse likePost(
      @PathVariable UUID postId,
      @Valid @RequestBody LikeDtos.LikeRequest request
  ) {
    Post post = postService.findActive(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    User user = userService.findByWalletAddress(request.walletAddress())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    ensureVerifiedUser(user);
    verifyWorldIdProof(WorldIdAction.POST_LIKE, request.proof(), user);
    boolean created = likeService.likePost(post, user);
    long count = likeService.postLikeCount(post);
    return new LikeResponse(created, count);
  }

  @DeleteMapping("/{postId}/likes")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unlikePost(
      @PathVariable UUID postId,
      @Valid @RequestBody LikeDtos.LikeRequest request
  ) {
    Post post = postService.findActive(postId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    User user = userService.findByWalletAddress(request.walletAddress())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    likeService.unlikePost(post, user);
  }

  public record LikeResponse(boolean created, long likeCount) {
  }

  private void verifyWorldIdProof(WorldIdAction action, WorldIdProofPayload proof, User user) {
    try {
      worldIdVerificationService.verifyAndReserve(action, proof, user);
    } catch (WorldIdVerificationException ex) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }
  }

  private void ensureVerifiedUser(User user) {
    if (user.getWorldVerifiedLevel() <= 0) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must be verified");
    }
  }
}
