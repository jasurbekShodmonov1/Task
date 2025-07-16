package com.example.task.dto.request;

public record BalanceTransactionRequestDto (
        Double amount,
        String description
){
}
