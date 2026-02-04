package com.fsilva.tc02.domain.user;

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
