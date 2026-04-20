package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Album;
import com.mobylab.springbackend.entity.Artist;
import com.mobylab.springbackend.entity.Genre;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.AlbumRepository;
import com.mobylab.springbackend.repository.ArtistRepository;
import com.mobylab.springbackend.repository.GenreRepository;
import com.mobylab.springbackend.repository.ReviewRepository;
import com.mobylab.springbackend.service.dto.AlbumRequestDto;
import com.mobylab.springbackend.service.dto.AlbumResponseDto;
import com.mobylab.springbackend.service.dto.GenreResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;
    private final ArtistService artistService;

    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository,
            GenreRepository genreRepository, ReviewRepository reviewRepository,
            ArtistService artistService) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
        this.reviewRepository = reviewRepository;
        this.artistService = artistService;
    }

    public AlbumResponseDto getById(UUID id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Album not found with id: " + id));
        return toResponseDto(album);
    }

    public List<AlbumResponseDto> getAll() {
        return albumRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<AlbumResponseDto> getByArtistId(UUID artistId) {
        return albumRepository.findByArtistId(artistId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<AlbumResponseDto> searchByTitle(String title) {
        return albumRepository.searchByTitle(title).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<AlbumResponseDto> getByGenreId(UUID genreId) {
        return albumRepository.findByGenreId(genreId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public AlbumResponseDto create(AlbumRequestDto dto) {
        Artist artist = artistRepository.findById(dto.getArtistId())
                .orElseThrow(() -> new NotFoundException("Artist not found with id: " + dto.getArtistId()));

        Album album = new Album()
                .setTitle(dto.getTitle())
                .setReleaseYear(dto.getReleaseYear())
                .setCoverImageUrl(dto.getCoverImageUrl())
                .setArtist(artist);

        if (dto.getGenreIds() != null && !dto.getGenreIds().isEmpty()) {
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(dto.getGenreIds()));
            album.setGenres(genres);
        }

        return toResponseDto(albumRepository.save(album));
    }

    public AlbumResponseDto update(UUID id, AlbumRequestDto dto) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Album not found with id: " + id));

        Artist artist = artistRepository.findById(dto.getArtistId())
                .orElseThrow(() -> new NotFoundException("Artist not found with id: " + dto.getArtistId()));

        album.setTitle(dto.getTitle())
                .setReleaseYear(dto.getReleaseYear())
                .setCoverImageUrl(dto.getCoverImageUrl())
                .setArtist(artist);

        if (dto.getGenreIds() != null) {
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(dto.getGenreIds()));
            album.setGenres(genres);
        }

        return toResponseDto(albumRepository.save(album));
    }

    public void delete(UUID id) {
        if (!albumRepository.existsById(id)) {
            throw new NotFoundException("Album not found with id: " + id);
        }
        albumRepository.deleteById(id);
    }

    private AlbumResponseDto toResponseDto(Album album) {
        Double avgRating = reviewRepository.getAverageRatingForAlbum(album.getId());
        List<GenreResponseDto> genreDtos = album.getGenres().stream()
                .map(g -> new GenreResponseDto().setId(g.getId()).setName(g.getName()))
                .collect(Collectors.toList());

        return new AlbumResponseDto()
                .setId(album.getId())
                .setTitle(album.getTitle())
                .setReleaseYear(album.getReleaseYear())
                .setCoverImageUrl(album.getCoverImageUrl())
                .setCreatedAt(album.getCreatedAt())
                .setArtist(artistService.toResponseDto(album.getArtist()))
                .setGenres(genreDtos)
                .setAverageRating(avgRating);
    }
}
