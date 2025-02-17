package com.crud.controller;

import com.crud.dto.LoginDTO;
import com.crud.dto.RegisterDTO;
import com.crud.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // Allow React Vite frontend
//@CrossOrigin(origins = "http://localhost:3000")  // Allow requests from React UI
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String welcome() {
        return "Welcome to the User API";
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody RegisterDTO registerDTO) {
        return userService.registerUser(registerDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestParam String email){
        return userService.forgetPassword(email);

    }
}
