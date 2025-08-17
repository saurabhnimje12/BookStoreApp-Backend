package com.example.controller;

/**
 * UserJwtController.java
 * This controller handles user-related operations such as getting, updating, and deleting users.
 * It uses JWT for authentication and authorization.
 */

import com.example.dto.DtoToUserEntity;
import com.example.dto.UserEntityToDto;
import com.example.exception.CustomiseException;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userApi")
@CrossOrigin(origins = "http://localhost:5173")
public class UserJwtController {
    private UserService userService;

    public UserJwtController(UserService userService) {
        this.userService = userService;
    }

    /**
     * This method retrieves the user details based on the userId extracted from the JWT token.
     *
     * @param userId The ID of the user to retrieve.
     * @return ResponseEntity containing UserEntityToDto object and HTTP status.
     */
    @GetMapping("/getUser")
    public ResponseEntity<UserEntityToDto> getUser(@RequestAttribute("userId") Long userId) {
        return new ResponseEntity<UserEntityToDto>(userService.getUser(userId), HttpStatus.OK);
    }

    /**
     * This method updates the user details based on the userId extracted from the JWT token.
     *
     * @param userId The ID of the user to update.
     * @param dtoToUserEntity The DTO containing updated user information.
     * @return ResponseEntity containing a success message and HTTP status.
     */
    @PatchMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestAttribute("userId") Long userId, @RequestBody DtoToUserEntity dtoToUserEntity) {
        return new ResponseEntity<String>(userService.updateUser(userId, dtoToUserEntity), HttpStatus.OK);
    }

    /**
     * This method deletes the user based on the userId extracted from the JWT token.
     *
     * @param userId The ID of the user to delete.
     * @return ResponseEntity containing a success message and HTTP status.
     */
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestAttribute("userId") Long userId) {
        return new ResponseEntity<String>(userService.deleteUser(userId), HttpStatus.OK);
    }

    /**
     * This method allows an admin to delete a user by their userId.
     *
     * @param role The role of the user making the request (should be ADMIN).
     * @param userId The ID of the user to delete.
     * @return ResponseEntity containing a success message or error message and HTTP status.
     */
    @DeleteMapping("/deleteByAdmin/{userId}")
    public ResponseEntity<String> deleteUserByAdmin(@RequestAttribute("role") String role, @PathVariable Long userId) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return new ResponseEntity<String>(userService.deleteUser(userId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Admin to Delete User!!", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * This method retrieves all users if the requester has an ADMIN role.
     *
     * @param role The role of the user making the request (should be ADMIN).
     * @return ResponseEntity containing a list of UserEntityToDto objects and HTTP status.
     */
    @GetMapping("/findAllUsers")
    public ResponseEntity<List<UserEntityToDto>> getAllUser(@RequestAttribute("role") String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return new ResponseEntity<List<UserEntityToDto>>(userService.getAllUser(), HttpStatus.OK);
        } else {
            throw new CustomiseException("NOT Valid to Access");
        }
    }
}
