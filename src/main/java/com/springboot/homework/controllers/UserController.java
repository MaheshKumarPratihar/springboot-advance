package com.springboot.homework.controllers;

import com.springboot.homework.models.dtos.UserDTO;
import com.springboot.homework.models.entities.User;
import com.springboot.homework.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserName = authentication.getName();
        User userInDb = this.userService.findByUserName(authenticatedUserName);

        if (userInDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in database.");
        }
        if (!authenticatedUserName.equals(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only modify your own data.");
        }

        userInDb.setPassword(userDTO.getPassword());

       this.userService.save(userInDb);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
