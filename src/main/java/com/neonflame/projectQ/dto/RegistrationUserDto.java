package com.neonflame.projectQ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserDto {
    private String email;
    private String username;
    private String password;
}
