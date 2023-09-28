package com.microservices.user.controller;

import com.microservices.user.request.UserRequest;
import com.microservices.user.response.UserResponse;
import com.microservices.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/songsWS-gabs-KBE/rest/auth")
public record UserController(UserService userService) {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authorize(@RequestBody UserRequest userRequest) {
        if (userRequest.getUserId().equals(null) || userRequest.getPassword().equals(null)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = userService.loginUser(userRequest);
        return new ResponseEntity<>(token, HttpStatus.OK);

    }

    @GetMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Iterable<UserResponse>> getAllUserResponses() {
        Iterable<UserResponse> allUsers = userService.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }
}

