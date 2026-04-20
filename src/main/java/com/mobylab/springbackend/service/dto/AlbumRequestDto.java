package com.mobylab.springbackend.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class AlbumRequestDto {

    @NotBlank(message = "Album title is required")
    private String title;
    private Integer releaseYear;
    private String coverImageUrl;

    @NotNull(message = "Artist ID is required")
    private UUID artistId;

    private List<UUID> genreIds;

    public String getTitle() {
        return title;
    }

    public AlbumRequestDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public AlbumRequestDto setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public AlbumRequestDto setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
        return this;
    }

    public UUID getArtistId() {
        return artistId;
    }

    public AlbumRequestDto setArtistId(UUID artistId) {
        this.artistId = artistId;
        return this;
    }

    public List<UUID> getGenreIds() {
        return genreIds;
    }

    public AlbumRequestDto setGenreIds(List<UUID> genreIds) {
        this.genreIds = genreIds;
        return this;
    }
}
