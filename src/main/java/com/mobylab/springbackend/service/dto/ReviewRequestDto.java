package com.mobylab.springbackend.service.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public class ReviewRequestDto {

    @NotBlank(message = "Review title is required")
    private String title;

    @NotBlank(message = "Review content is required")
    private String content;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be at most 10")
    private Integer rating;

    @NotNull(message = "Album ID is required")
    private UUID albumId;

    public String getTitle() {
        return title;
    }

    public ReviewRequestDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ReviewRequestDto setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getRating() {
        return rating;
    }

    public ReviewRequestDto setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public UUID getAlbumId() {
        return albumId;
    }

    public ReviewRequestDto setAlbumId(UUID albumId) {
        this.albumId = albumId;
        return this;
    }
}
