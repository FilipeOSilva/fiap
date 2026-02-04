package com.fsilva.tc02.mapper;

import com.fsilva.tc02.dto.UserResponseDTO;
import com.fsilva.tc02.domain.user.User;

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
