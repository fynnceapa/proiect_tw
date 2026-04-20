package com.mobylab.springbackend.service.dto;

import jakarta.validation.constraints.NotBlank;

public class ArtistRequestDto {

    @NotBlank(message = "Artist name is required")
    private String name;
    private String bio;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public ArtistRequestDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public ArtistRequestDto setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArtistRequestDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
