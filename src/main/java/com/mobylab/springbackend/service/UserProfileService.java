package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.entity.UserProfile;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.UserProfileRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.UserProfileRequestDto;
import com.mobylab.springbackend.service.dto.UserProfileResponseDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public UserProfileResponseDto getByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        User currentUser = null;
        try {
            currentUser = getCurrentUser();
        } catch (Exception ignored) {
        }

        return toResponseDto(user, currentUser);
    }

    public UserProfileResponseDto getMyProfile() {
        User currentUser = getCurrentUser();
        return toResponseDto(currentUser, currentUser);
    }

    public UserProfileResponseDto updateMyProfile(UserProfileRequestDto dto) {
        User currentUser = getCurrentUser();
        UserProfile profile = userProfileRepository.findByUserId(currentUser.getId())
                .orElse(new UserProfile().setUser(currentUser));

        profile.setDisplayName(dto.getDisplayName())
                .setBio(dto.getBio())
                .setAvatarUrl(dto.getAvatarUrl());

        userProfileRepository.save(profile);
        return toResponseDto(currentUser, currentUser);
    }

    private UserProfileResponseDto toResponseDto(User user, User currentUser) {
        UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);

        boolean followedByCurrentUser = false;
        if (currentUser != null && !currentUser.getId().equals(user.getId())) {
            followedByCurrentUser = currentUser.getFollowing().stream()
                    .anyMatch(u -> u.getId().equals(user.getId()));
        }

        return new UserProfileResponseDto()
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setDisplayName(profile != null ? profile.getDisplayName() : user.getUsername())
                .setBio(profile != null ? profile.getBio() : null)
                .setAvatarUrl(profile != null ? profile.getAvatarUrl() : null)
                .setFollowerCount(user.getFollowers().size())
                .setFollowingCount(user.getFollowing().size())
                .setReviewCount(user.getReviews().size())
                .setFollowedByCurrentUser(followedByCurrentUser);
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Current user not found"));
    }
}
