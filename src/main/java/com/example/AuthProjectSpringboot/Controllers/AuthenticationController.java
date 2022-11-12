package com.example.AuthProjectSpringboot.Controllers;

import com.example.AuthProjectSpringboot.DTO.UserLoginDTO;
import com.example.AuthProjectSpringboot.Entities.User;
import com.example.AuthProjectSpringboot.Services.AuthenticationService;
import com.example.AuthProjectSpringboot.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;

    public AuthenticationController() {
        //this.authService = AuthenticationService.getInstance();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO login) {
        String email = login.email;
        String password = login.password;

        if (!Utils.isEmailValid(email)) {
            return ResponseEntity.badRequest().header("errorMessage","email in wrong format").build();
        }
        if (!Utils.isPasswordValid(password)) {
            return ResponseEntity.badRequest().header("errorMessage","password in wrong format").build();
        }
        try {
            ResponseEntity<String> tokenResponse = ResponseEntity.ok(authService.login(email, password));
            authService.addToken(tokenResponse.getBody(), authService.getUserByEmail(email));
            return tokenResponse;
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<User> register(@RequestBody User user) {
        System.out.println(user.email);
        System.out.println(user.name);
        System.out.println(user.password);
        if (!Utils.isEmailValid(user.email) || !Utils.isNameValid(user.name) || !Utils.isPasswordValid(user.password)){
            System.out.println("before bad request");
            return ResponseEntity.badRequest().header("errorMessage","registration failed. details are not in correct format").build();
        }

        User registeredUser = authService.register(user);
        if (registeredUser == null) {
            return ResponseEntity.badRequest().header("errorMessage", "Email already exists").build();
        }
        System.out.println("before ok");
        return ResponseEntity.ok(registeredUser);
    }

    //test
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("hello");
    }
}