package com.fsilva.tc01.mapper;

import com.fsilva.tc01.dto.UserResponseDTO;
import com.fsilva.tc01.domain.user.User;

public class UserMapper {

    private UserMapper() {}

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
