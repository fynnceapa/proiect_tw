package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.FollowService;
import com.mobylab.springbackend.service.UserProfileService;
import com.mobylab.springbackend.service.dto.UserProfileRequestDto;
import com.mobylab.springbackend.service.dto.UserProfileResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserProfileController implements SecuredRestController {

    private final UserProfileService userProfileService;
    private final FollowService followService;

    public UserProfileController(UserProfileService userProfileService, FollowService followService) {
        this.userProfileService = userProfileService;
        this.followService = followService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<UserProfileResponseDto> getMyProfile() {
        return ResponseEntity.ok(userProfileService.getMyProfile());
    }

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<UserProfileResponseDto> updateMyProfile(@RequestBody UserProfileRequestDto dto) {
        return ResponseEntity.ok(userProfileService.updateMyProfile(dto));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(userProfileService.getByUserId(userId));
    }

    @PostMapping("/follow/{userId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> follow(@PathVariable UUID userId) {
        followService.follow(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/follow/{userId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> unfollow(@PathVariable UUID userId) {
        followService.unfollow(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<UserProfileResponseDto>> getFollowers(@PathVariable UUID userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<UserProfileResponseDto>> getFollowing(@PathVariable UUID userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }
}
