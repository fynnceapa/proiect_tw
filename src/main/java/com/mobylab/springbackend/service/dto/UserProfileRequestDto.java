package com.mobylab.springbackend.service.dto;

public class UserProfileRequestDto {

    private String displayName;
    private String bio;
    private String avatarUrl;

    public String getDisplayName() {
        return displayName;
    }

    public UserProfileRequestDto setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public UserProfileRequestDto setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserProfileRequestDto setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }
}
