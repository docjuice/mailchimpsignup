package com.service;

import com.service.entity.User;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class UserManager {

    private User user;
    private String userPropertiesFilePath;

    private final static String URL = "https://login.mailchimp.com/signup/post";

    
    public UserManager(String userPropertiesFilePath) {

        this.userPropertiesFilePath = userPropertiesFilePath;

        try {
            FileInputStream fileInputStream = new FileInputStream(userPropertiesFilePath);

            Properties properties = new Properties();
            properties.load(fileInputStream);

            this.user = new User(properties.getProperty("username"), properties.getProperty("password"), properties.getProperty("email"));

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


    public boolean signUp() {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        if (user != null && validateUser(user, validator)) {
            try {
                CloseableHttpClient client = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(URL);

                post.setHeader("User-Agent", USER_AGENT);

                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("email", user.getEmail()));
                urlParameters.add(new BasicNameValuePair("username", user.getUsername()));
                urlParameters.add(new BasicNameValuePair("password", user.getPassword()));

                post.setEntity(new UrlEncodedFormEntity(urlParameters));

                HttpResponse response = client.execute(post);

                int resultStatus = response.getStatusLine().getStatusCode();

                switch (resultStatus) {
                    /* Mailchimp returns 302(MOVED_TEMPORARILY) status when user has been registered*/
                    case HttpStatus.SC_MOVED_TEMPORARILY:
                        return true;
                    case HttpStatus.SC_OK:
                        System.out.println("Could not sign up the user:\n\tAnother user with this username already exists.");
                        break;
                    default:
                        break;
                }

                client.close();
            } catch (IOException e) {
                System.out.println("Error while accessing Mailchimp server " +  e.getMessage());
            }
        }

        return false;
    }

    public static boolean validateUser(Object object, Validator validator) {

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

        if (!constraintViolations.isEmpty()) {

            System.out.println("Could not sign up the user:");

            for (ConstraintViolation constraintViolation : constraintViolations) {
                System.out.println(constraintViolation.getMessage());
            }

            return false;
        } else {
            return true;
        }
    }
}
