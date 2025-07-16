package com.example.task.dto.auth;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record TokenInfoResponse(
    UUID userId, String token, String username) {}
