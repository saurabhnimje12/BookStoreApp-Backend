package com.example.service;

import com.example.dto.DtoToUserEntity;
import com.example.dto.UserEntityToDto;
import com.example.dto.UserLoginRequestDto;
import com.example.entity.User;
import com.example.exception.CustomiseException;
import com.example.repo.UserRepo;
import com.example.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private
    UserServiceImpl userService;

    @Test
    void userRegistration_success() {
        DtoToUserEntity dto = new DtoToUserEntity();
        dto.setEmail("test@example.com");

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        String result = userService.userRegistration(dto);

        assertThat(result).isEqualTo("User registered successfully!!");
        verify(userRepo).save(any(User.class));
    }

    @Test
    void userRegistration_emailExists() {
        DtoToUserEntity dto = new DtoToUserEntity();
        dto.setEmail("test@example.com");

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        String result = userService.userRegistration(dto);

        assertThat(result).isEqualTo("Email ID Already Taken | Try With Different Email");
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void loginService_success() {
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("pass");

        when(userRepo.findByEmailAndPassword("test@example.com", "pass"))
                .thenReturn(Optional.of(new User()));

        Optional<User> result = userService.loginService(loginDto);

        assertThat(result).isPresent();
    }

    @Test
    void loginService_failure() {
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setEmail("nope@example.com");
        loginDto.setPassword("wrong");

        when(userRepo.findByEmailAndPassword(any(), any())).thenReturn(Optional.empty());

        Optional<User> result = userService.loginService(loginDto);

        assertThat(result).isEmpty();
    }

    @Test
    void getUser_found() {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("John");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        UserEntityToDto dto = userService.getUser(1L);

        assertThat(dto.getUserId()).isEqualTo(1L);
        assertThat(dto.getFirstName()).isEqualTo("John");
    }

    @Test
    void getUser_notFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomiseException.class, () -> userService.getUser(1L));
    }

    @Test
    void updateUser_success() {
        User existingUser = new User();
        existingUser.setUserId(1L);

        DtoToUserEntity updateDto = new DtoToUserEntity();
        updateDto.setFirstName("Updated");
        updateDto.setLastName("User");
        updateDto.setDob(LocalDate.of(1990, 1, 1));
        updateDto.setEmail("updated@example.com");
        updateDto.setPassword("newpass");
        updateDto.setRole("ADMIN");

        when(userRepo.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        String result = userService.updateUser(1L, updateDto);

        assertThat(result).isEqualTo("User Updated Successfully!!");
        verify(userRepo).save(any(User.class));
    }

    @Test
    void updateUser_notFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomiseException.class, () -> userService.updateUser(1L, new DtoToUserEntity()));
    }

    @Test
    void deleteUser_success() {
        User user = new User();
        user.setUserId(1L);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        String result = userService.deleteUser(1L);

        assertThat(result).isEqualTo("User Deleted Successfully!!");
        verify(userRepo).deleteById(1L);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomiseException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void getAllUser_success() {
        User u1 = new User();
        u1.setUserId(1L);
        u1.setFirstName("John");

        User u2 = new User();
        u2.setUserId(2L);
        u2.setFirstName("Jane");

        when(userRepo.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<UserEntityToDto> result = userService.getAllUser();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }
}
