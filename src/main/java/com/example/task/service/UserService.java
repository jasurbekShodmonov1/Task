package com.example.task.service;


import com.example.task.dto.request.UserRequestDto;
import com.example.task.dto.response.UserResponseDto;
import com.example.task.entity.User;
import com.example.task.mapper.UserMapper;
import com.example.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponseDto> getAllUser(){
        List<User> users = userRepository.findAll();

        return users.stream().map(userMapper :: toDto).toList();

    }

    public UserResponseDto createUser(UserRequestDto userRequestDto){
        User user = userMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));

        User saved = userRepository.save(user);

        return userMapper.toDto(saved);
    }

//    public String  addMoney(UUID id, Double amount){
//
//        User user = userRepository.findById(id)
//                        .orElseThrow(()->new RuntimeException("User not found"));
//        user.setBalance(user.getBalance()+amount);
//        userRepository.save(user);
//
//        return "Money added successfully";
//
//    }



    public  boolean deleteUser(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));

        userRepository.delete(user);

        return  true;
    }
}
