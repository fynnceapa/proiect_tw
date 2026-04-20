package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Artist;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.ArtistRepository;
import com.mobylab.springbackend.service.dto.ArtistRequestDto;
import com.mobylab.springbackend.service.dto.ArtistResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public ArtistResponseDto getById(UUID id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artist not found with id: " + id));
        return toResponseDto(artist);
    }

    public List<ArtistResponseDto> getAll() {
        return artistRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ArtistResponseDto> search(String name) {
        return artistRepository.searchByName(name).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public ArtistResponseDto create(ArtistRequestDto dto) {
        Artist artist = new Artist()
                .setName(dto.getName())
                .setBio(dto.getBio())
                .setImageUrl(dto.getImageUrl());
        return toResponseDto(artistRepository.save(artist));
    }

    public ArtistResponseDto update(UUID id, ArtistRequestDto dto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artist not found with id: " + id));
        artist.setName(dto.getName())
                .setBio(dto.getBio())
                .setImageUrl(dto.getImageUrl());
        return toResponseDto(artistRepository.save(artist));
    }

    public void delete(UUID id) {
        if (!artistRepository.existsById(id)) {
            throw new NotFoundException("Artist not found with id: " + id);
        }
        artistRepository.deleteById(id);
    }

    public ArtistResponseDto toResponseDto(Artist artist) {
        return new ArtistResponseDto()
                .setId(artist.getId())
                .setName(artist.getName())
                .setBio(artist.getBio())
                .setImageUrl(artist.getImageUrl())
                .setCreatedAt(artist.getCreatedAt());
    }
}
