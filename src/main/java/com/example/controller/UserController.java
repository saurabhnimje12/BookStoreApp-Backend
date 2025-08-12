package com.example.controller;

import com.example.dto.DtoToUserEntity;
import com.example.dto.UserLoginRequestDto;
import com.example.entity.JwtResponse;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.utils.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    private TokenUtility tokenUtility;

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/userRegistration")
    public ResponseEntity<String> userRegistration(@RequestBody DtoToUserEntity dtoToUserEntity) {
        String msg = userService.userRegistration(dtoToUserEntity);
        if (msg.equals("User Added Successfully!!")){
            return new ResponseEntity<String>(msg, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        Optional<User> userOptional = userService.loginService(userLoginRequestDto);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(new JwtResponse(tokenUtility.createToken(userOptional.get().getUserId(), userOptional.get().getRole())));
        } else {
            return new ResponseEntity<>("User login not successfully", HttpStatus.ACCEPTED);
        }
    }
}