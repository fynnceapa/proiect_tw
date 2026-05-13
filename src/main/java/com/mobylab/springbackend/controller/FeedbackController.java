package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.FeedbackService;
import com.mobylab.springbackend.service.dto.FeedbackRequestDto;
import com.mobylab.springbackend.service.dto.FeedbackResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController implements SecuredRestController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<FeedbackResponseDto> create(@Valid @RequestBody FeedbackRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<FeedbackResponseDto>> getAll() {
        return ResponseEntity.ok(feedbackService.getAll());
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<FeedbackResponseDto> setRead(@PathVariable UUID id, @RequestParam boolean read) {
        return ResponseEntity.ok(feedbackService.setRead(id, read));
    }
}
