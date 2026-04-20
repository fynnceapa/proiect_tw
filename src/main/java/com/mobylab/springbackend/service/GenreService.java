package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Genre;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.GenreRepository;
import com.mobylab.springbackend.service.dto.GenreRequestDto;
import com.mobylab.springbackend.service.dto.GenreResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public GenreResponseDto getById(UUID id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found with id: " + id));
        return toResponseDto(genre);
    }

    public List<GenreResponseDto> getAll() {
        return genreRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public GenreResponseDto create(GenreRequestDto dto) {
        if (genreRepository.findByName(dto.getName()).isPresent()) {
            throw new BadRequestException("Genre already exists: " + dto.getName());
        }
        Genre genre = new Genre().setName(dto.getName());
        return toResponseDto(genreRepository.save(genre));
    }

    public void delete(UUID id) {
        if (!genreRepository.existsById(id)) {
            throw new NotFoundException("Genre not found with id: " + id);
        }
        genreRepository.deleteById(id);
    }

    public GenreResponseDto toResponseDto(Genre genre) {
        return new GenreResponseDto()
                .setId(genre.getId())
                .setName(genre.getName());
    }
}
