package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<Album, UUID> {

    @Query("SELECT a FROM Album a WHERE a.artist.id = :artistId")
    List<Album> findByArtistId(@Param("artistId") UUID artistId);

    @Query("SELECT a FROM Album a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Album> searchByTitle(@Param("title") String title);

    @Query("SELECT DISTINCT a FROM Album a JOIN a.genres g WHERE g.id = :genreId")
    List<Album> findByGenreId(@Param("genreId") UUID genreId);
}
