package com.example.task.service;


import com.example.task.dto.request.UserRequestDto;
import com.example.task.dto.response.UserResponseDto;
import com.example.task.entity.User;
import com.example.task.mapper.UserMapper;
import com.example.task.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetAllUser(){
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setFullName("Alice Johnson");
        user1.setBalance(new BigDecimal("100.00"));
        user1.setUsername("alice");
        user1.setPassword("");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setFullName("Bob Smith");
        user2.setBalance(new BigDecimal("200.00"));
        user2.setUsername("bob");

        List<User> users = List.of(user1,user2);
        when(userRepository.findAll()).thenReturn(users);

        UserResponseDto dto1 = new UserResponseDto(user1.getId(),"Alice Johnson", 100.00, "alice");
        UserResponseDto dto2 = new UserResponseDto(user2.getId(),"Bob Smith", 200.00, "bob");

        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        List<UserResponseDto> result = userService.getAllUser();

        assertEquals(2,result.size());
        assertEquals("Alice Johnson", result.get(0).fullName());
        assertEquals("Bob Smith", result.get(1).fullName());

        assertEquals("alice", result.get(0).username());
        assertEquals(100.0, result.get(0).balance());

        verify(userRepository).findAll();
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    void createUserShouldReturnMappedResponse(){
        UserRequestDto userRequestDto = new UserRequestDto("Alice", 500.0, "alice123", "mypassword");

        User mappedUser = mock(User.class);
        User savedUser = mock(User.class);

        UserResponseDto responseDto = new UserResponseDto(UUID.randomUUID(), "Alice", 500.0, "alice123");

        when(userMapper.toEntity(userRequestDto)).thenReturn(mappedUser);
        when(passwordEncoder.encode("mypassword")).thenReturn("encoded_pass");
        when(userRepository.save(mappedUser)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.createUser(userRequestDto);

        assertNotNull(result);
        assertEquals("Alice", result.fullName());
        assertEquals(500, result.balance());
        assertEquals("alice123", result.username());

        verify(userMapper).toEntity(userRequestDto);
        verify(passwordEncoder).encode("mypassword");
        verify(mappedUser).setPassword("encoded_pass");
        verify(userRepository).save(mappedUser);
        verify(userMapper).toDto(savedUser);
    }


}
