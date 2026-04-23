package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_profiles", schema = "project")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public UUID getId() {
        return id;
    }

    public UserProfile setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserProfile setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public UserProfile setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserProfile setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public User getUser() {
        return user;
    }

    public UserProfile setUser(User user) {
        this.user = user;
        return this;
    }
}
