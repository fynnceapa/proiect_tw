package com.mobylab.springbackend.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AlbumResponseDto {

    private UUID id;
    private String title;
    private Integer releaseYear;
    private String coverImageUrl;
    private LocalDateTime createdAt;
    private ArtistResponseDto artist;
    private List<GenreResponseDto> genres;
    private Double averageRating;

    public UUID getId() {
        return id;
    }

    public AlbumResponseDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AlbumResponseDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public AlbumResponseDto setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public AlbumResponseDto setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AlbumResponseDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ArtistResponseDto getArtist() {
        return artist;
    }

    public AlbumResponseDto setArtist(ArtistResponseDto artist) {
        this.artist = artist;
        return this;
    }

    public List<GenreResponseDto> getGenres() {
        return genres;
    }

    public AlbumResponseDto setGenres(List<GenreResponseDto> genres) {
        this.genres = genres;
        return this;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public AlbumResponseDto setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
        return this;
    }
}
