package com.example.controller;

import com.example.dto.DtoToUserEntity;
import com.example.dto.UserEntityToDto;
import com.example.exception.CustomiseException;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserJwtControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserJwtController userJwtController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser() {
        UserEntityToDto mockUser = new UserEntityToDto();
        mockUser.setUserId(1L);
        mockUser.setFirstName("John");

        when(userService.getUser(1L)).thenReturn(mockUser);

        ResponseEntity<UserEntityToDto> response = userJwtController.getUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).getUser(1L);
    }

    @Test
    void testUpdateUser() {
        DtoToUserEntity dto = new DtoToUserEntity();
        when(userService.updateUser(eq(1L), any(DtoToUserEntity.class))).thenReturn("Updated");

        ResponseEntity<String> response = userJwtController.updateUser(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated", response.getBody());
        verify(userService, times(1)).updateUser(eq(1L), any(DtoToUserEntity.class));
    }

    @Test
    void testDeleteUser() {
        when(userService.deleteUser(1L)).thenReturn("Deleted");

        ResponseEntity<String> response = userJwtController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted", response.getBody());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testDeleteUserByAdmin_WhenRoleIsAdmin() {
        when(userService.deleteUser(2L)).thenReturn("Deleted by Admin");

        ResponseEntity<String> response = userJwtController.deleteUserByAdmin("ADMIN", 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted by Admin", response.getBody());
        verify(userService, times(1)).deleteUser(2L);
    }

    @Test
    void testDeleteUserByAdmin_WhenRoleIsNotAdmin() {
        ResponseEntity<String> response = userJwtController.deleteUserByAdmin("USER", 2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid Admin to Delete User!!", response.getBody());
        verify(userService, never()).deleteUser(anyLong());
    }

    @Test
    void testGetAllUser_WhenRoleIsAdmin() {
        List<UserEntityToDto> users = Arrays.asList(new UserEntityToDto(), new UserEntityToDto());
        when(userService.getAllUser()).thenReturn(users);

        ResponseEntity<List<UserEntityToDto>> response = userJwtController.getAllUser("ADMIN");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getAllUser();
    }

    @Test
    void testGetAllUser_WhenRoleIsNotAdmin_ShouldThrowException() {
        assertThrows(CustomiseException.class, () -> {
            userJwtController.getAllUser("USER");
        });

        verify(userService, never()).getAllUser();
    }
}
