package com.springboot.homework.services;

import com.springboot.homework.models.entities.Role;
import com.springboot.homework.models.entities.User;
import com.springboot.homework.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User Not Found!");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .map(String::toUpperCase)
                        .toArray(String[]::new))
                .build();
    }
}
