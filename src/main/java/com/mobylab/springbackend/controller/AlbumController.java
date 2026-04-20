package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.AlbumService;
import com.mobylab.springbackend.service.dto.AlbumRequestDto;
import com.mobylab.springbackend.service.dto.AlbumResponseDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/albums")
@SecurityRequirement(name = "bearerAuth")
public class AlbumController implements SecuredRestController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<AlbumResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(albumService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<AlbumResponseDto>> getAll() {
        return ResponseEntity.ok(albumService.getAll());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<AlbumResponseDto>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(albumService.searchByTitle(title));
    }

    @GetMapping("/artist/{artistId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<AlbumResponseDto>> getByArtistId(@PathVariable UUID artistId) {
        return ResponseEntity.ok(albumService.getByArtistId(artistId));
    }

    @GetMapping("/genre/{genreId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<AlbumResponseDto>> getByGenreId(@PathVariable UUID genreId) {
        return ResponseEntity.ok(albumService.getByGenreId(genreId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AlbumResponseDto> create(@Valid @RequestBody AlbumRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(albumService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AlbumResponseDto> update(@PathVariable UUID id, @Valid @RequestBody AlbumRequestDto dto) {
        return ResponseEntity.ok(albumService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
