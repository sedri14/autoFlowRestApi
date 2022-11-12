package com.example.AuthProjectSpringboot.Services;


import com.example.AuthProjectSpringboot.Entities.User;
import com.example.AuthProjectSpringboot.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    boolean createUser(User user) {
        return true;
    }

    public User updateEmail(User user, String updatedEmail) throws IOException {
        userRepo.deleteFile(user);
        User newUser = new User(user.getId(), updatedEmail, user.getName(), user.getPassword());
        updateData(newUser);
        return newUser;
    }

    public User updateName(User user, String updatedName) throws IOException {
        userRepo.deleteFile(user);
        User newUser = new User(user.getId(), user.getEmail(), updatedName, user.getPassword());
        updateData(newUser);
        return newUser;
    }

    public User updatePassword(User user, String updatedPassword) throws IOException {
        userRepo.deleteFile(user);
        User newUser = new User(user.getId(), user.getEmail(), user.getName(), updatedPassword);
        updateData(newUser);
        return newUser;
    }

    public User deleteUser(User user) {
        userRepo.deleteFile(user);
        return user;
    }


    public void updateData(User user) throws IOException {
        userRepo.writeToFile(user.getEmail() + ".json", user);
    }
}
