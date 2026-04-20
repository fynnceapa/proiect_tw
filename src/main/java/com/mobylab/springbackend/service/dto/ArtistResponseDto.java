package com.mobylab.springbackend.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ArtistResponseDto {

    private UUID id;
    private String name;
    private String bio;
    private String imageUrl;
    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public ArtistResponseDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ArtistResponseDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public ArtistResponseDto setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArtistResponseDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ArtistResponseDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
