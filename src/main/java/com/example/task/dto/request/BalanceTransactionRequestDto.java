package com.example.task.dto.request;

import com.example.task.entity.enums.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record BalanceTransactionRequestDto (
        BigDecimal amount,
        String description,

        UUID senderUserId,
        UUID receiverUserId
){
}
