package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC")
    List<Review> findAllOrdered();

    @Query("SELECT r FROM Review r WHERE r.album.id = :albumId ORDER BY r.createdAt DESC")
    List<Review> findByAlbumId(@Param("albumId") UUID albumId);

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Review> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT r FROM Review r WHERE r.user.id IN " +
            "(SELECT f.id FROM User u JOIN u.following f WHERE u.id = :userId) " +
            "ORDER BY r.createdAt DESC")
    List<Review> findFeedForUser(@Param("userId") UUID userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.album.id = :albumId")
    Double getAverageRatingForAlbum(@Param("albumId") UUID albumId);
}
