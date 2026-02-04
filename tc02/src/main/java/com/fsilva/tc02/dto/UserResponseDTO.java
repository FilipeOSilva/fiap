package com.fsilva.tc02.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private LocalDateTime lastUpdate;

    public UserResponseDTO(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
