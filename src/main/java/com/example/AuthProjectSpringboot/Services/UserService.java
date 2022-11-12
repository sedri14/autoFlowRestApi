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

    private UserService() {
        //userRepo = UserRepository.getInstance();
    }

//    public static UserService getInstance() {
//
//        UserService result = userService;
//
//        if (result == null) {
//            synchronized (UserService.class) {
//                result = userService;
//                if (result == null) {
//                    userService = result = new UserService();
//                }
//            }
//        }
//        return result;
//    }

    boolean createUser(User user) {
        return true;
    }

    public boolean updateEmail(User user, String updatedEmail) throws IOException {
        userRepo.deleteFile(user);
        User newUser = new User(user.getId(), updatedEmail, user.getName(), user.getPassword());
        updateData(newUser);
        return true;
    }

    public boolean updateName(User user, String updatedName) throws IOException {
        userRepo.deleteFile(user);
        User newUser = new User(user.getId(), user.getEmail(), updatedName, user.getPassword());
        updateData(newUser);
        return true;
    }

    public boolean updatePassword(User user, String updatedPassword) throws IOException {
        userRepo.deleteFile(user);
        User newUser = new User(user.getId(), user.getEmail(), user.getName(), updatedPassword);
        updateData(newUser);
        return true;
    }

    public boolean deleteUser(User user) {
        userRepo.deleteFile(user);
        return true;
    }


    public void updateData(User user) throws IOException {
        userRepo.writeToFile(user.getEmail() + ".json", user);
    }
}
