package com.example.AuthProjectSpringboot.Controllers;

import com.example.AuthProjectSpringboot.DTO.UserUpdateDTO;
import com.example.AuthProjectSpringboot.Entities.User;
import com.example.AuthProjectSpringboot.Services.AuthenticationService;
import com.example.AuthProjectSpringboot.Services.UserService;
import com.example.AuthProjectSpringboot.Utils.Utils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidParameterException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationService authService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/update/email", method = RequestMethod.PATCH, consumes = "application/json")
    public ResponseEntity<User> updateEmail(@RequestBody UserUpdateDTO userUpdate) throws IOException {

        String token = userUpdate.token;
        String newEmail = userUpdate.email;

        if (!Utils.isEmailValid(newEmail)) {
            return ResponseEntity.badRequest().header("errorMessage", "email in wrong format").build();
        }

        User user;
        try {
            user = authService.validate(token);
        } catch (InvalidParameterException e) {
            return ResponseEntity.badRequest().header("errorMessage", e.getMessage()).build();
        }

        user = userService.updateEmail(user, newEmail);    //update user in repository
        authService.reloadUser(newEmail, token);           //update user in cache

        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/update/name", method = RequestMethod.PATCH, consumes = "application/json")
    public ResponseEntity<User> updateName(@RequestBody UserUpdateDTO userUpdate) throws IOException {
        String token = userUpdate.token;
        String newName = userUpdate.name;

        if (!Utils.isNameValid(newName)) {
            return ResponseEntity.badRequest().header("errorMessage", "name in wrong format").build();
        }

        User user;
        try {
            user = authService.validate(token);
        } catch (InvalidParameterException e) {
            return ResponseEntity.badRequest().header("errorMessage", e.getMessage()).build();
        }

        user = userService.updateName(user, newName);
        authService.reloadUser(user.getEmail(), token);

        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/update/password", method = RequestMethod.PATCH, consumes = "application/json")
    public ResponseEntity<User> updatePassword(@RequestBody UserUpdateDTO userUpdate) throws IOException {
        String token = userUpdate.token;
        String newPassword = userUpdate.password;

        if (!Utils.isPasswordValid(newPassword)) {
            return ResponseEntity.badRequest().header("errorMessage", "password in wrong format").build();
        }

        User user;
        try {
            user = authService.validate(token);
        } catch (InvalidParameterException e) {
            return ResponseEntity.badRequest().header("errorMessage", e.getMessage()).build();
        }

        user = userService.updatePassword(user, newPassword);
        authService.reloadUser(user.getEmail(), token);

        return ResponseEntity.ok(user);
    }

//    public boolean deleteUser(String token) {
//        User user = authService.validate(token);
//        userService.deleteUser(user);
//        authService.removeToken(token);
//
//        return true;
//    }

}

