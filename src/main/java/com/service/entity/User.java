package com.service.entity;

import javax.validation.constraints.*;

public class User {

    @NotNull(message = "Username is compulsory")
    @NotBlank(message = "Username is compulsory")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]*$",
             message = "Username should contain only letters and numbers or use your email address")
    private String username;

    @NotNull(message = "Password is compulsory")
    @NotBlank(message = "Password is compulsory")
    @Size(min = 8, message = "Password should be 8 characters minimum")
    @Pattern(regexp = "^.*(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!;:*()@#$%^&+-=_<>]).*$",
             message = "Password should contain:\n\t1.) One lowercase character\n\t2.) One uppercase character\n\t3.) One number\n\t4.) One special character")
    private String password;

    @NotNull(message = "Email address is compulsory")
    @NotBlank(message = "Email address is compulsory")
    @Email(message = "Wrong email format")
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    private Boolean isCorrectUsername(String username) {
        return username.matches("");
    }

    private Boolean isCorrectPassword(String password) {
        return password.matches("");
    }

    private Boolean isCorrectEmail(String email) {
        return email.matches("");
    }
}
