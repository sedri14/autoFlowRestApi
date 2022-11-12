package com.example.AuthProjectSpringboot.Utils;

import java.io.File;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isEmailValid(String temp) {
        boolean matches = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$").matcher(temp).matches();
        return matches;
    }

    public static boolean isPasswordValid(String pass) {
        return (pass != null && !pass.equals(""));
    }

    public static boolean isNameValid(String name) {
        return (name.length() <= 10);
    }

    public static boolean isJsonFile(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".")).equals(".json");
    }
}
