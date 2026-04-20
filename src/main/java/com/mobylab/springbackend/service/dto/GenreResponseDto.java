package com.mobylab.springbackend.service.dto;

import java.util.UUID;

public class GenreResponseDto {

    private UUID id;
    private String name;

    public UUID getId() {
        return id;
    }

    public GenreResponseDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GenreResponseDto setName(String name) {
        this.name = name;
        return this;
    }
}
