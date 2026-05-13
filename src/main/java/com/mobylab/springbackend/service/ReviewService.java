package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Album;
import com.mobylab.springbackend.entity.Review;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.ForbiddenException;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.AlbumRepository;
import com.mobylab.springbackend.repository.ReviewRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.ReviewRequestDto;
import com.mobylab.springbackend.service.dto.ReviewResponseDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public ReviewService(ReviewRepository reviewRepository, AlbumRepository albumRepository,
            UserRepository userRepository, EmailService emailService) {
        this.reviewRepository = reviewRepository;
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public ReviewResponseDto getById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + id));
        return toResponseDto(review);
    }

    public List<ReviewResponseDto> getAll() {
        return reviewRepository.findAllOrdered().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getByAlbumId(UUID albumId) {
        return reviewRepository.findByAlbumId(albumId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getByUserId(UUID userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getFeed() {
        User currentUser = getCurrentUser();
        return reviewRepository.findFeedForUser(currentUser.getId()).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public ReviewResponseDto create(ReviewRequestDto dto) {
        User currentUser = getCurrentUser();
        Album album = albumRepository.findById(dto.getAlbumId())
                .orElseThrow(() -> new NotFoundException("Album not found with id: " + dto.getAlbumId()));

        Review review = new Review()
                .setTitle(dto.getTitle())
                .setContent(dto.getContent())
                .setRating(dto.getRating())
                .setUser(currentUser)
                .setAlbum(album);

        ReviewResponseDto response = toResponseDto(reviewRepository.save(review));

        // Send email notification via MailTrap
        emailService.sendNewReviewNotification(
                currentUser.getEmail(),
                currentUser.getUsername(),
                album.getTitle(),
                dto.getRating());

        return response;
    }

    public ReviewResponseDto update(UUID id, ReviewRequestDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + id));

        User currentUser = getCurrentUser();
        if (!review.getUser().getId().equals(currentUser.getId()) && !isAdmin(currentUser)) {
            throw new ForbiddenException("You can only edit your own reviews");
        }

        review.setTitle(dto.getTitle())
                .setContent(dto.getContent())
                .setRating(dto.getRating())
                .setUpdatedAt(LocalDateTime.now());

        return toResponseDto(reviewRepository.save(review));
    }

    public void delete(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + id));

        User currentUser = getCurrentUser();
        if (!review.getUser().getId().equals(currentUser.getId()) && !isAdmin(currentUser)) {
            throw new ForbiddenException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    public void likeReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        User currentUser = getCurrentUser();
        review.getLikedBy().add(currentUser);
        reviewRepository.save(review);
    }

    public void unlikeReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        User currentUser = getCurrentUser();
        review.getLikedBy().remove(currentUser);
        reviewRepository.save(review);
    }

    private ReviewResponseDto toResponseDto(Review review) {
        User currentUser = null;
        try {
            currentUser = getCurrentUser();
        } catch (Exception ignored) {
        }

        boolean likedByCurrentUser = false;
        if (currentUser != null) {
            User finalCurrentUser = currentUser;
            likedByCurrentUser = review.getLikedBy().stream()
                    .anyMatch(u -> u.getId().equals(finalCurrentUser.getId()));
        }

        return new ReviewResponseDto()
                .setId(review.getId())
                .setTitle(review.getTitle())
                .setContent(review.getContent())
                .setRating(review.getRating())
                .setCreatedAt(review.getCreatedAt())
                .setUpdatedAt(review.getUpdatedAt())
                .setUsername(review.getUser().getUsername())
                .setUserId(review.getUser().getId())
                .setAlbumId(review.getAlbum().getId())
                .setAlbumTitle(review.getAlbum().getTitle())
                .setLikeCount(review.getLikedBy().size())
                .setCommentCount(review.getComments().size())
                .setLikedByCurrentUser(likedByCurrentUser);
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Current user not found"));
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getName()));
    }
}
