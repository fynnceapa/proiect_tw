package com.mobylab.springbackend.service.dto;

import jakarta.validation.constraints.NotBlank;

public class GenreRequestDto {

    @NotBlank(message = "Genre name is required")
    private String name;

    public String getName() {
        return name;
    }

    public GenreRequestDto setName(String name) {
        this.name = name;
        return this;
    }
}
