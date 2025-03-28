package io.github.rtheodoro4201.eventconnect.controller;

import io.github.rtheodoro4201.eventconnect.dto.user.UserLoginDTO;
import io.github.rtheodoro4201.eventconnect.dto.user.UserRegistrationDTO;
import io.github.rtheodoro4201.eventconnect.dto.user.UserUpdateDTO;
import io.github.rtheodoro4201.eventconnect.model.User;
import io.github.rtheodoro4201.eventconnect.service.UserService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegistrationDTO registrationDTO) {
        User registeredUser = userService.registerUser(registrationDTO.getName(), registrationDTO.getEmail(), registrationDTO.getPassword());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        Optional<User> userOptional = userService.loginUser(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        if (userOptional.isPresent()) {
            return ResponseEntity.ok("Successful login");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        return userOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDTO updateDTO) {
        Optional<User> updatedUser = userService.updateUser(userId, updateDTO.getName(), updateDTO.getEmail(), updateDTO.getPassword());
        return updatedUser.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId)
    {
        if(userService.deleteUser(userId))
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        throw new ResourceNotFoundException("User " + userId + " not found.");
    }
}