package com.naome.template.user.mappers;

import com.naome.template.auth.dtos.RegisterRequestDTO;
import com.naome.template.user.User;
import com.naome.template.user.dtos.UserResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(RegisterRequestDTO userDto);
    UserResponseDTO toResponseDTO(User user);
}
