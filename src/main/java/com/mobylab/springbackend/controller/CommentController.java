package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.CommentService;
import com.mobylab.springbackend.service.dto.CommentRequestDto;
import com.mobylab.springbackend.service.dto.CommentResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController implements SecuredRestController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/review/{reviewId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<CommentResponseDto>> getByReviewId(@PathVariable UUID reviewId) {
        return ResponseEntity.ok(commentService.getByReviewId(reviewId));
    }

    @PostMapping("/review/{reviewId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<CommentResponseDto> create(@PathVariable UUID reviewId,
            @Valid @RequestBody CommentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(reviewId, dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<CommentResponseDto> update(@PathVariable UUID id, @Valid @RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
