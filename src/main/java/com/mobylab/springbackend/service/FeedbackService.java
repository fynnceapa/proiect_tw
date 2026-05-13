package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Feedback;
import com.mobylab.springbackend.entity.Review;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.FeedbackRepository;
import com.mobylab.springbackend.repository.ReviewRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.FeedbackRequestDto;
import com.mobylab.springbackend.service.dto.FeedbackResponseDto;
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
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, ReviewRepository reviewRepository,
            UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public FeedbackResponseDto create(FeedbackRequestDto dto) {
        User currentUser = getCurrentUser();
        Feedback feedback = new Feedback()
                .setTopic(dto.getTopic())
                .setTone(dto.getTone())
                .setAllowContact(Boolean.TRUE.equals(dto.getAllowContact()))
                .setMessage(dto.getMessage())
                .setUser(currentUser);

        if (dto.getReviewId() != null) {
            Review review = reviewRepository.findById(dto.getReviewId())
                    .orElseThrow(() -> new NotFoundException("Review not found with id: " + dto.getReviewId()));
            feedback.setReview(review);
        }

        return toResponseDto(feedbackRepository.save(feedback));
    }

    public List<FeedbackResponseDto> getAll() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public FeedbackResponseDto setRead(UUID id, boolean read) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Feedback not found with id: " + id));
        feedback.setRead(read);
        feedback.setReadAt(read ? LocalDateTime.now() : null);
        return toResponseDto(feedbackRepository.save(feedback));
    }

    private FeedbackResponseDto toResponseDto(Feedback feedback) {
        Review review = feedback.getReview();
        return new FeedbackResponseDto()
                .setId(feedback.getId())
                .setTopic(feedback.getTopic())
                .setTone(feedback.getTone())
                .setAllowContact(feedback.isAllowContact())
                .setMessage(feedback.getMessage())
                .setCreatedAt(feedback.getCreatedAt())
                .setRead(feedback.isRead())
                .setReadAt(feedback.getReadAt())
                .setUserId(feedback.getUser().getId())
                .setUsername(feedback.getUser().getUsername())
                .setReviewId(review != null ? review.getId() : null)
                .setReviewTitle(review != null ? review.getTitle() : null)
                .setAlbumTitle(review != null ? review.getAlbum().getTitle() : null);
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Current user not found"));
    }
}
