package com.mobylab.springbackend.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "genres", schema = "project")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public UUID getId() {
        return id;
    }

    public Genre setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Genre setName(String name) {
        this.name = name;
        return this;
    }
}
