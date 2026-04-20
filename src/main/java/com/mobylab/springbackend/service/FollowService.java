package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.UserProfileResponseDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FollowService {

    private final UserRepository userRepository;

    public FollowService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void follow(UUID targetUserId) {
        User currentUser = getCurrentUser();
        if (currentUser.getId().equals(targetUserId)) {
            throw new BadRequestException("You cannot follow yourself");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + targetUserId));

        currentUser.getFollowing().add(targetUser);
        userRepository.save(currentUser);
    }

    public void unfollow(UUID targetUserId) {
        User currentUser = getCurrentUser();
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + targetUserId));

        currentUser.getFollowing().remove(targetUser);
        userRepository.save(currentUser);
    }

    public List<UserProfileResponseDto> getFollowers(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        return user.getFollowers().stream()
                .map(this::toSimpleProfileDto)
                .collect(Collectors.toList());
    }

    public List<UserProfileResponseDto> getFollowing(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        return user.getFollowing().stream()
                .map(this::toSimpleProfileDto)
                .collect(Collectors.toList());
    }

    private UserProfileResponseDto toSimpleProfileDto(User user) {
        return new UserProfileResponseDto()
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setFollowerCount(user.getFollowers().size())
                .setFollowingCount(user.getFollowing().size())
                .setReviewCount(user.getReviews().size());
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Current user not found"));
    }
}
