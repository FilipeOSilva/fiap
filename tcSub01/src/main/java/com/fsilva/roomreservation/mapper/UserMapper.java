package com.fsilva.roomreservation.mapper;

import com.fsilva.roomreservation.domain.user.User;
import com.fsilva.roomreservation.dto.user.LoginResponse;
import com.fsilva.roomreservation.dto.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt());
    }

    public LoginResponse toLoginResponse(User user) {
        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                "Login validado com sucesso");
    }
}
