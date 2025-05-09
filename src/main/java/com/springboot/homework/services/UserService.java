package com.springboot.homework.services;

import com.springboot.homework.configurations.JwtTokenProvider;
import com.springboot.homework.models.dtos.UserDTO;
import com.springboot.homework.models.entities.Role;
import com.springboot.homework.models.entities.User;
import com.springboot.homework.repositories.RoleRepository;
import com.springboot.homework.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public User findByUserName(String userName) {
        return this.userRepository.findByUsername(userName);
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
    }

    public void saveNewUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = this.convertToUser(userDTO);
        Set<Role> roles = this.roleRepository.findByNameIn(userDTO.getRoles());
        user.setRoles(roles);
        this.userRepository.save(user);
    }

    public String verify(UserDTO userDTO) {

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
        );
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authentication.getPrincipal();

        List<String> roles = user.getAuthorities().stream()
                .map(Object::toString)
                .toList();

        if(authentication.isAuthenticated()){
            return this.jwtTokenProvider.generateToken(userDTO.getUsername(), roles);
        }else {
            return "Fail";
        }
    }

    public User convertToUser(UserDTO userDTO){
        return User.builder()
                .fullName(userDTO.getFullName())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();
    }
}
