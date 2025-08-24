package com.example.repo;

import com.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepoTest {

    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        userRepo = Mockito.mock(UserRepo.class);
    }

    @Test
    void testFindByEmail_Success() {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userRepo.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void testFindByEmail_Failure() {
        when(userRepo.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userRepo.findByEmail("notfound@example.com");

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByEmailAndPassword_Success() {
        User user = new User();
        user.setUserId(2L);
        user.setEmail("john@example.com");
        user.setPassword("secret");

        when(userRepo.findByEmailAndPassword("john@example.com", "secret"))
                .thenReturn(Optional.of(user));

        Optional<User> result = userRepo.findByEmailAndPassword("john@example.com", "secret");

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
        assertEquals("secret", result.get().getPassword());
    }

    @Test
    void testFindByEmailAndPassword_Failure() {
        when(userRepo.findByEmailAndPassword("wrong@example.com", "wrongpass"))
                .thenReturn(Optional.empty());

        Optional<User> result = userRepo.findByEmailAndPassword("wrong@example.com", "wrongpass");

        assertFalse(result.isPresent());
    }
}
