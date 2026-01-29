package com.example.demo.web.controller;

import com.example.demo.domain.user.User;
import com.example.demo.service.UserService;
import com.example.demo.web.dto.UserDtos;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.worldid.WorldIdVerificationService.WorldIdVerificationException;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDtos.UserResponse upsertUser(@Valid @RequestBody UserDtos.UpsertUserRequest request) {
    User user = userService.upsertUser(request.walletAddress(), request.username());
    return UserDtos.UserResponse.from(user);
  }

  @PostMapping("/{userId}/verify")
  public UserDtos.UserResponse verifyUser(
      @PathVariable UUID userId,
      @Valid @RequestBody UserDtos.VerifyUserRequest request
  ) {
    try {
      User user = userService.verifyUser(userId, request.proof());
      return UserDtos.UserResponse.from(user);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
    } catch (WorldIdVerificationException ex) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }
  }

  @GetMapping("/{userId}")
  public UserDtos.UserResponse getUser(@PathVariable UUID userId) {
    return userService.findById(userId)
        .map(UserDtos.UserResponse::from)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }
}
