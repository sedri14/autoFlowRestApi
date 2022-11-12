package com.example.AuthProjectSpringboot.Services;

import com.example.AuthProjectSpringboot.Entities.User;
import com.example.AuthProjectSpringboot.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepo;
    Map<String, User> userTokens;

    private AuthenticationService() {
        this.userTokens = new HashMap<>();
        //userRepo = UserRepository.getInstance();
    }

//    public static AuthenticationService getInstance() {
//
//        AuthenticationService result = authService;
//
//        if (result == null) {
//            synchronized (AuthenticationService.class) {
//                result = authService;
//                if (result == null) {
//                    authService = result = new AuthenticationService();
//                }
//            }
//        }
//        return result;
//    }

    public User register(User user) {

        if (!checkIfUserExists(user.email)) {
            User userWithId = new User(UUID.randomUUID().hashCode(), user.email, user.name, user.password);
            try {
                userRepo.writeToFile(userWithId.getEmail() + ".json", userWithId);
                return userWithId;
            } catch (IOException e) {
                System.out.println("Couldn't write to file");
                throw new RuntimeException(e);
            }
        }else{
            return null;
        }
    }

    public User validate(String token) {
        if (!userTokens.containsKey(token)) {
            throw new InvalidParameterException("Token incorrect");
        }
        return userTokens.get(token);
    }


    public String login(String email, String password) {
        User cachedUser = userRepo.readFromCache(email);
        if (cachedUser == null) {
            throw new IllegalArgumentException("user doesn\"t exist");
        } else if (!Objects.equals(cachedUser.getPassword(), password)) {
            throw new IllegalArgumentException("wrong password");
        }
        return createToken(cachedUser);
    }

    private String createToken(User user) {
        String token = UUID.randomUUID().toString();
        userTokens.put(token, user);
        return token;
    }

    public void reloadUser(String email, String token) {
        User updatedUser = userRepo.readFromCache(email);
        userTokens.put(token, updatedUser);
    }

    public void removeToken(String token) {
        userTokens.remove(token);
    }

    public void addToken(String token, User user){
        userTokens.put(token,user);
    }

    public User getUserByEmail(String email){
        return userRepo.getUserByEmail(email);
    }

    public boolean checkIfUserExists(String email) {

        return userRepo.isUserExists(email);
//        try (FileReader fr = new FileReader(email + ".json")) {
//        } catch (FileNotFoundException e) {
//            return false;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return true;
    }
}

