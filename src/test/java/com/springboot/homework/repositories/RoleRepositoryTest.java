package com.springboot.homework.repositories;

import com.springboot.homework.models.entities.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByName() {
        Role userRole = roleRepository.findByName("USER");

        assertThat(userRole).isNotNull();
        assertThat(userRole.getName()).isEqualTo("USER");
    }

    @Test
    void testFindByNameShouldReturnNullForNonExistentRole() {
        Role nonExistentRole = roleRepository.findByName("INVALID_ROLE");

        assertThat(nonExistentRole).isNull();
    }

    @Test
    void testFindByNameIn() {
        List<String> roleNames = List.of("USER", "ADMIN");

        Set<Role> roles = roleRepository.findByNameIn(roleNames);

        assertThat(roles).isNotNull();
        assertThat(roles).hasSize(2);
        assertThat(roles).extracting("name").containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    void testFindByNameInWithNoMatches() {
        List<String> roleNames = List.of("INVALID_ROLE_1", "INVALID_ROLE_2");

        Set<Role> roles = roleRepository.findByNameIn(roleNames);

        assertThat(roles).isEmpty();
    }
}