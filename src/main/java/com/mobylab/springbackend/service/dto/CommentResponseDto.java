package com.mobylab.springbackend.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentResponseDto {

    private UUID id;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private UUID userId;
    private UUID reviewId;

    public UUID getId() {
        return id;
    }

    public CommentResponseDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CommentResponseDto setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CommentResponseDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CommentResponseDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public UUID getUserId() {
        return userId;
    }

    public CommentResponseDto setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public UUID getReviewId() {
        return reviewId;
    }

    public CommentResponseDto setReviewId(UUID reviewId) {
        this.reviewId = reviewId;
        return this;
    }
}
