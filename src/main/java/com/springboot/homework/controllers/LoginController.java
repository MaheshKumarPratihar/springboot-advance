package com.springboot.homework.controllers;

import com.springboot.homework.models.dtos.UserDTO;
import com.springboot.homework.models.entities.User;
import com.springboot.homework.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDTO user){
        this.userService.saveNewUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO user){
        return this.userService.verify(user);
    }
}
