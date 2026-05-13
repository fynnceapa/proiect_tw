package com.mobylab.springbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "feedback", schema = "project")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "tone", nullable = false)
    private String tone;

    @Column(name = "allow_contact", nullable = false)
    private boolean allowContact = false;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "read", nullable = false)
    private boolean read = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public UUID getId() {
        return id;
    }

    public Feedback setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public Feedback setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getTone() {
        return tone;
    }

    public Feedback setTone(String tone) {
        this.tone = tone;
        return this;
    }

    public boolean isAllowContact() {
        return allowContact;
    }

    public Feedback setAllowContact(boolean allowContact) {
        this.allowContact = allowContact;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Feedback setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Feedback setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isRead() {
        return read;
    }

    public Feedback setRead(boolean read) {
        this.read = read;
        return this;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public Feedback setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Feedback setUser(User user) {
        this.user = user;
        return this;
    }

    public Review getReview() {
        return review;
    }

    public Feedback setReview(Review review) {
        this.review = review;
        return this;
    }
}
