package com.example.controller;

import com.example.controller.UserController;
import com.example.dto.DtoToUserEntity;
import com.example.dto.UserLoginRequestDto;
import com.example.entity.JwtResponse;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.utils.TokenUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenUtility tokenUtility;

    @Test
    void userRegistration_success() throws Exception {
        DtoToUserEntity dto = new DtoToUserEntity();
        Mockito.when(userService.userRegistration(any())).thenReturn("User Added Successfully!!");

        mockMvc.perform(post("/user/userRegistration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User Added Successfully!!"));
    }

    @Test
    void userRegistration_failure() throws Exception {
        DtoToUserEntity dto = new DtoToUserEntity();
        Mockito.when(userService.userRegistration(any())).thenReturn("Email ID Already Taken | Try With Different Email");

        mockMvc.perform(post("/user/userRegistration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email ID Already Taken | Try With Different Email"));
    }

    @Test
    void authenticateUser_success() throws Exception {
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("pass");

        User user = new User();
        user.setUserId(1L);
        user.setRole("USER");

        Mockito.when(userService.loginService(any())).thenReturn(Optional.of(user));
        Mockito.when(tokenUtility.createToken(1L, "USER")).thenReturn("mock-token");

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"));
    }

    @Test
    void authenticateUser_failure() throws Exception {
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setEmail("fail@example.com");
        loginDto.setPassword("fail");

        Mockito.when(userService.loginService(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("User login not successfully"));
    }
}
