package com.example.task.dto.auth;


import com.example.task.exception.UnauthorizedException;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.Set;
import java.util.UUID;

@Builder
public record UserDtoResponse(
    UUID id,
    String username,


    @Nullable Boolean isActive) {


}
