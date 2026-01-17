package com.fsilva.tc01.dto;

import com.fsilva.tc01.domain.user.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
