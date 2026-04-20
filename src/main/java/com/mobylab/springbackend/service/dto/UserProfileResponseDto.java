package com.mobylab.springbackend.service.dto;

import java.util.UUID;

public class UserProfileResponseDto {

    private UUID userId;
    private String username;
    private String email;
    private String displayName;
    private String bio;
    private String avatarUrl;
    private int followerCount;
    private int followingCount;
    private int reviewCount;
    private boolean followedByCurrentUser;

    public UUID getUserId() {
        return userId;
    }

    public UserProfileResponseDto setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserProfileResponseDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserProfileResponseDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserProfileResponseDto setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public UserProfileResponseDto setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserProfileResponseDto setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public UserProfileResponseDto setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
        return this;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public UserProfileResponseDto setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
        return this;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public UserProfileResponseDto setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
        return this;
    }

    public boolean isFollowedByCurrentUser() {
        return followedByCurrentUser;
    }

    public UserProfileResponseDto setFollowedByCurrentUser(boolean followedByCurrentUser) {
        this.followedByCurrentUser = followedByCurrentUser;
        return this;
    }
}
