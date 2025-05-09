package com.springboot.homework.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class UserDTO {
    private String fullName;
    private String username;
    private String password;
    private List<String> roles = new ArrayList<>();
}
