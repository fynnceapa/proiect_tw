package com.mobylab.springbackend.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class FeedbackResponseDto {

    private UUID id;
    private String topic;
    private String tone;
    private boolean allowContact;
    private String message;
    private LocalDateTime createdAt;
    private boolean read;
    private LocalDateTime readAt;
    private UUID userId;
    private String username;
    private UUID reviewId;
    private String reviewTitle;
    private String albumTitle;

    public UUID getId() {
        return id;
    }

    public FeedbackResponseDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public FeedbackResponseDto setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getTone() {
        return tone;
    }

    public FeedbackResponseDto setTone(String tone) {
        this.tone = tone;
        return this;
    }

    public boolean isAllowContact() {
        return allowContact;
    }

    public FeedbackResponseDto setAllowContact(boolean allowContact) {
        this.allowContact = allowContact;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public FeedbackResponseDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public FeedbackResponseDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isRead() {
        return read;
    }

    public FeedbackResponseDto setRead(boolean read) {
        this.read = read;
        return this;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public FeedbackResponseDto setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
        return this;
    }

    public UUID getUserId() {
        return userId;
    }

    public FeedbackResponseDto setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public FeedbackResponseDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public UUID getReviewId() {
        return reviewId;
    }

    public FeedbackResponseDto setReviewId(UUID reviewId) {
        this.reviewId = reviewId;
        return this;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public FeedbackResponseDto setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
        return this;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public FeedbackResponseDto setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
        return this;
    }
}
