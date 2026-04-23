package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "project")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", schema = "project", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_follows", schema = "project", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following", fetch = FetchType.LAZY)
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "likedBy", fetch = FetchType.LAZY)
    private Set<Review> likedReviews = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public User setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public User setProfile(UserProfile profile) {
        this.profile = profile;
        return this;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public User setReviews(List<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public User setComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public User setFollowing(Set<User> following) {
        this.following = following;
        return this;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public User setFollowers(Set<User> followers) {
        this.followers = followers;
        return this;
    }

    public Set<Review> getLikedReviews() {
        return likedReviews;
    }

    public User setLikedReviews(Set<Review> likedReviews) {
        this.likedReviews = likedReviews;
        return this;
    }
}
