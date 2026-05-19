package com.example.task.controller;


import com.example.task.config.CurrentUser;
import com.example.task.dto.auth.TokenInfoResponse;
import com.example.task.dto.request.LoginDto;
import com.example.task.dto.response.UserResponseDto;
import com.example.task.entity.User;
import com.example.task.mapper.UserMapper;
import com.example.task.service.auth.AuthTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthTokenService authTokenService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@CurrentUser User user) {
        log.info(user.toString());
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfoResponse> login(@Valid @RequestBody LoginDto loginDto) {
        System.out.println(loginDto.username());
        var token = authTokenService.generateToken(loginDto);
        System.out.println(token);
        return ResponseEntity.ok(token);

    }
}
