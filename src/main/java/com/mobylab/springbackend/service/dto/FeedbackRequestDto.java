package com.mobylab.springbackend.service.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class FeedbackRequestDto {

    @NotBlank(message = "Topic is required")
    private String topic;

    @NotBlank(message = "Tone is required")
    private String tone;

    private Boolean allowContact;

    @NotBlank(message = "Message is required")
    private String message;

    private UUID reviewId;

    public String getTopic() {
        return topic;
    }

    public FeedbackRequestDto setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getTone() {
        return tone;
    }

    public FeedbackRequestDto setTone(String tone) {
        this.tone = tone;
        return this;
    }

    public Boolean getAllowContact() {
        return allowContact;
    }

    public FeedbackRequestDto setAllowContact(Boolean allowContact) {
        this.allowContact = allowContact;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public FeedbackRequestDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public UUID getReviewId() {
        return reviewId;
    }

    public FeedbackRequestDto setReviewId(UUID reviewId) {
        this.reviewId = reviewId;
        return this;
    }
}
