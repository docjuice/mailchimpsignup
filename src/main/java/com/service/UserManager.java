package com.service;

import com.service.entity.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class UserManager {

    private User user;
    private String userPropertiesFilePath;

    public UserManager(String userPropertiesFilePath) {

        this.userPropertiesFilePath = userPropertiesFilePath;

        try {
            FileInputStream fileInputStream = new FileInputStream(userPropertiesFilePath);

            Properties properties = new Properties();
            properties.load(fileInputStream);

            User user = new User(properties.getProperty("username"), properties.getProperty("password"), properties.getProperty("email"));
            this.user = user;

            fileInputStream.close();

        } catch (IOException e) {
            System.out.println("Error while reading user properties: " +  e.getMessage());
        }
    }

    public User getUser() {
        return user;
    }

    public String getUserPropertiesFilePath() {
        return userPropertiesFilePath;
    }


    public void signUp() {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        if (validateUser(user, validator)) {

        }

    }

    public static boolean validateUser(Object object, Validator validator) {

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

        if (!constraintViolations.isEmpty()) {

            System.out.println("Could not sign up the user:\n");

            for (ConstraintViolation constraintViolation : constraintViolations) {
                System.out.println(" --- " + constraintViolation.getInvalidValue());
                System.out.println(constraintViolation.getMessage());
            }
            return false;
        } else {
            return true;
        }
    }
}
