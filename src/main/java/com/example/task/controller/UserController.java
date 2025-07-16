package com.example.task.controller;


import com.example.task.dto.request.UserRequestDto;
import com.example.task.dto.response.UserResponseDto;
import com.example.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        List<UserResponseDto> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto user){
        UserResponseDto createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

//    @PatchMapping("/{userId}addMoney    ")
//    public String addMoney(@PathVariable("userId") UUID id, @RequestParam Double amount){
//        ;
//
//        return userService.addMoney(id, amount);
//    }

    @DeleteMapping("/{userId}")
    public boolean deleteUser(@PathVariable("userId") UUID id){
        return userService.deleteUser(id);
    }
}
