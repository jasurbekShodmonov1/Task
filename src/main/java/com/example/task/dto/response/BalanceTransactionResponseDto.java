package com.example.task.dto.response;

import com.example.task.entity.enums.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record BalanceTransactionResponseDto(
        UUID id,
        BigDecimal amount,
        String description,

        UserResponseDto senderUser,
        UserResponseDto receiverUser
) {
}
