package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.GenreService;
import com.mobylab.springbackend.service.dto.GenreRequestDto;
import com.mobylab.springbackend.service.dto.GenreResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreController implements SecuredRestController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<GenreResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(genreService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<GenreResponseDto>> getAll() {
        return ResponseEntity.ok(genreService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenreResponseDto> create(@Valid @RequestBody GenreRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.create(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
