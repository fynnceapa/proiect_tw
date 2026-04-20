package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Comment;
import com.mobylab.springbackend.entity.Review;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.ForbiddenException;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.CommentRepository;
import com.mobylab.springbackend.repository.ReviewRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.CommentRequestDto;
import com.mobylab.springbackend.service.dto.CommentResponseDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ReviewRepository reviewRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public List<CommentResponseDto> getByReviewId(UUID reviewId) {
        return commentRepository.findByReviewId(reviewId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public CommentResponseDto create(UUID reviewId, CommentRequestDto dto) {
        User currentUser = getCurrentUser();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        Comment comment = new Comment()
                .setContent(dto.getContent())
                .setUser(currentUser)
                .setReview(review);

        return toResponseDto(commentRepository.save(comment));
    }

    public CommentResponseDto update(UUID id, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + id));

        User currentUser = getCurrentUser();
        if (!comment.getUser().getId().equals(currentUser.getId()) && !isAdmin(currentUser)) {
            throw new ForbiddenException("You can only edit your own comments");
        }

        comment.setContent(dto.getContent());
        return toResponseDto(commentRepository.save(comment));
    }

    public void delete(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + id));

        User currentUser = getCurrentUser();
        if (!comment.getUser().getId().equals(currentUser.getId()) && !isAdmin(currentUser)) {
            throw new ForbiddenException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    private CommentResponseDto toResponseDto(Comment comment) {
        return new CommentResponseDto()
                .setId(comment.getId())
                .setContent(comment.getContent())
                .setCreatedAt(comment.getCreatedAt())
                .setUsername(comment.getUser().getUsername())
                .setUserId(comment.getUser().getId())
                .setReviewId(comment.getReview().getId());
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
