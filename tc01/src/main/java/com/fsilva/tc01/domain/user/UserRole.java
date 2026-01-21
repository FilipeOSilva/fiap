package com.fsilva.tc01.domain.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public enum UserRole {
    OWNER("owner"),
    USER("user");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

}
