package com.example.controller;

/**
 * UserController.java
 * This class is a Spring REST controller that handles user-related operations such as registration and login.
 * It uses UserService to perform business logic and TokenUtility to generate JWT tokens.
 */

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

    /**
     * User Registration
     * This method is used to register a new user.
     * It takes a DtoToUserEntity object containing user details and calls the userService to save the user.
     * If the user is added successfully, it returns a success message with HTTP status 201 (Created).
     * If there is an error, it returns an error message with HTTP status 400 (Bad Request).
     * @param dtoToUserEntity
     * @return
     */
    @PostMapping("/userRegistration")
    public ResponseEntity<String> userRegistration(@RequestBody DtoToUserEntity dtoToUserEntity) {
        String msg = userService.userRegistration(dtoToUserEntity);
        if (msg.equals("User Added Successfully!!")){
            return new ResponseEntity<String>(msg, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * User Login
     * This method is used to authenticate a user.
     * It takes a UserLoginRequestDto object containing login credentials and calls the userService to validate the user.
     * If the user is found, it generates a JWT token and returns it in the response.
     * If the login fails, it returns an error message with HTTP status 202 (Accepted).
     * @param userLoginRequestDto
     * @return
     */
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