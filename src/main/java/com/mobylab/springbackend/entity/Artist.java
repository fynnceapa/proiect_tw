package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "artists", schema = "project")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "bio")
    private String bio;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albums = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public Artist setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Artist setName(String name) {
        this.name = name;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public Artist setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Artist setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Artist setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public Artist setAlbums(List<Album> albums) {
        this.albums = albums;
        return this;
    }
}
