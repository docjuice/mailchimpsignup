package com.service;

import java.util.Arrays;

public class Application {

    public static void main(String[] args) {

        if (args.length == 1) {

            UserManager userManager = new UserManager(args[0]);

            if (userManager.signUp()) {
                System.out.println("You have been successfully signed up! Please check your email to activate your account.");
            } else {
                System.out.println("Please change user registration info and try again.");
            }
        } else {
            System.out.println("Please specify user settings file.");
        }
    }
}
