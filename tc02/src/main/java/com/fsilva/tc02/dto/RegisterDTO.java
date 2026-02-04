package com.fsilva.tc02.dto;

import com.fsilva.tc02.domain.user.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
