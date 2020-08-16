package com.example.assignment2.Classess;

import androidx.annotation.NonNull;

public class User_Model {
    private String user_id;
    private String name,username,User_password,User_dob,User_age,User_hobbies, User_gender;
    private String User_image;


    // empty constructor required...
    public User_Model() {
    }

    // user registration constructor user_id not required becasue we are doing user_id uniq and auto increment...
    public User_Model(@NonNull String UserId, String name, String username, String user_password, String user_dob, String user_age, String user_hobbies, String user_gender, String user_image) {
        this.user_id = UserId;
        this.name = name;
        this.username = username;
        User_password = user_password;
        User_dob = user_dob;
        User_age = user_age;
        User_hobbies = user_hobbies;
        User_gender = user_gender;
        User_image = user_image;
    }
// getting user registration information along with user_id for showing currently login user information by user_id..

    // user login contrctor with username and password only
    public User_Model(String username, String user_password) {
        this.username = username;
        User_password = user_password;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_password() {
        return User_password;
    }

    public void setUser_password(String user_password) {
        User_password = user_password;
    }

    public String getUser_dob() {
        return User_dob;
    }

    public void setUser_dob(String user_dob) {
        User_dob = user_dob;
    }

    public String getUser_age() {
        return User_age;
    }

    public void setUser_age(String user_age) {
        User_age = user_age;
    }

    public String getUser_hobbies() {
        return User_hobbies;
    }

    public void setUser_hobbies(String user_hobbies) {
        User_hobbies = user_hobbies;
    }

    public String getUser_gender() {
        return User_gender;
    }

    public void setUser_gender(String user_gender) {
        User_gender = user_gender;
    }

    public String getUser_image() {
        return User_image;
    }

    public void setUser_image(String user_image) {
        User_image = user_image;
    }
}
