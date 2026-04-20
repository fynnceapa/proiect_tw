package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.ArtistService;
import com.mobylab.springbackend.service.dto.ArtistRequestDto;
import com.mobylab.springbackend.service.dto.ArtistResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/artists")
public class ArtistController implements SecuredRestController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<ArtistResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(artistService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<ArtistResponseDto>> getAll() {
        return ResponseEntity.ok(artistService.getAll());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<ArtistResponseDto>> search(@RequestParam String name) {
        return ResponseEntity.ok(artistService.search(name));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ArtistResponseDto> create(@Valid @RequestBody ArtistRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ArtistResponseDto> update(@PathVariable UUID id, @Valid @RequestBody ArtistRequestDto dto) {
        return ResponseEntity.ok(artistService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        artistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
