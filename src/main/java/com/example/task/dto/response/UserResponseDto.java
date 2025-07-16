package com.example.task.dto.response;

import java.io.DataOutput;
import java.math.BigDecimal;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String fullName,
        Double balance,
        String username
) {
}
