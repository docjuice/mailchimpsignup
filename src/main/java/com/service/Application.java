package com.service;

public class Application {

    public static void main(String[] args) {

        UserManager userManager = new UserManager(/*args[0]*/ "user.txt");

        userManager.signUp();

    }
}
