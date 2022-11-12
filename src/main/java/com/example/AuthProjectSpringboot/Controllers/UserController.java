package com.example.AuthProjectSpringboot.Controllers;

import com.example.AuthProjectSpringboot.Entities.User;
import com.example.AuthProjectSpringboot.Services.AuthenticationService;
import com.example.AuthProjectSpringboot.Services.UserService;
import com.example.AuthProjectSpringboot.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public UserController() {
        //this.authService = AuthenticationService.getInstance();
        //this.userService = UserService.getInstance();
    }



    public boolean updateEmail(String mail, String token) throws IOException {
        try{
            Utils.isEmailValid(mail);
        }catch (InvalidParameterException ip){
            throw new InvalidParameterException("Email not in correct format");
        }
        User user = authService.validate(token);
        boolean status = userService.updateEmail(user, mail);
        if (status) {
            authService.reloadUser(mail, token);
        }
        return status;
    }

    public boolean updateName(String name, String token) throws IOException {
        try{
            Utils.isNameValid(name);
        }catch (InvalidParameterException ip){
            throw new InvalidParameterException("Name not in correct format");
        }
        User user = authService.validate(token);
        boolean status = userService.updateName(user, name);
        if (status) {
            authService.reloadUser(user.getEmail(), token);
        }
        return status;
    }
    public boolean updatePassword(String password, String token) throws IOException {
        try{
            Utils.isPasswordValid(password);
        }catch (InvalidParameterException ip){
            throw new InvalidParameterException("Email not in correct format");
        }
        User user = authService.validate(token);
        boolean status = userService.updatePassword(user, password);
        if (status) {
            authService.reloadUser(user.getEmail(), token);
        }
        return status;
    }

    public boolean deleteUser(String token){
        User user = authService.validate(token);
        boolean status = userService.deleteUser(user);
        if (status) {
            authService.removeToken(token);
        }
        return status;
    }

}

