package com.example.task.dto.request;



public record UserRequestDto(
        String fullName,
        Double balance,
        String username,
        String password
) {
}
