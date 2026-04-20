package com.mobylab.springbackend.service.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentRequestDto {

    @NotBlank(message = "Comment content is required")
    private String content;

    public String getContent() {
        return content;
    }

    public CommentRequestDto setContent(String content) {
        this.content = content;
        return this;
    }
}
