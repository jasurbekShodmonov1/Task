package com.example.task.mapper;


import com.example.task.dto.request.UserRequestDto;
import com.example.task.dto.response.UserResponseDto;
import com.example.task.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDto userRequestDto);

    UserResponseDto toDto(User user);
}
