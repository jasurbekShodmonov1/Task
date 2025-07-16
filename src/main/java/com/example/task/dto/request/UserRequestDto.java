package com.example.task.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record UserRequestDto(
        String fullName,
        Double balance,
        String username,
        String password
) {
}
