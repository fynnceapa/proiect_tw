package com.mobylab.springbackend.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewResponseDto {

    private UUID id;
    private String title;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username;
    private UUID userId;
    private UUID albumId;
    private String albumTitle;
    private int likeCount;
    private int commentCount;
    private boolean likedByCurrentUser;

    public UUID getId() {
        return id;
    }

    public ReviewResponseDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ReviewResponseDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ReviewResponseDto setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getRating() {
        return rating;
    }

    public ReviewResponseDto setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReviewResponseDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ReviewResponseDto setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ReviewResponseDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public UUID getUserId() {
        return userId;
    }

    public ReviewResponseDto setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public UUID getAlbumId() {
        return albumId;
    }

    public ReviewResponseDto setAlbumId(UUID albumId) {
        this.albumId = albumId;
        return this;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public ReviewResponseDto setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
        return this;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public ReviewResponseDto setLikeCount(int likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public ReviewResponseDto setCommentCount(int commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public ReviewResponseDto setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
        return this;
    }
}
