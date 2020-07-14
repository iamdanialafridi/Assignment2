package com.example.assignment2.Classess;

import androidx.annotation.NonNull;

public class User_Model {
    private int user_id;
    private String name,username,User_password,User_dob,User_age,User_hobbies,User_gender;
    private byte[] User_image;


    public static  final String TABLE_NAME = "USERS";

    public static  final String KEY_USER_ID = "USER_ID";
    public static  final String KEY_USER_NAME = "NAME";
    public static  final String KEY_USERNAME = "USERNAME";
    public static  final String KEY_USER_PASSWORD = "PASSWORD";
    public static  final String KEY_USER_DOB = "DOB";
    public static  final String KEY_USER_AGE = "AGE";
    public static  final String KEY_USER_HOBBIES = "HOBBIES";
    public static  final String KEY_USER_GENDER = "GENDER";
    public static  final String KEY_USER_IMG = "USER_IMG";
    public static final String CREATE_TBL = String.format("CREATE TABLE IF NOT EXISTS " + " %s(%s INTEGER PRIMARY KEY AUTOINCREMENT ,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s BLOB)",
            TABLE_NAME,KEY_USER_ID,KEY_USER_NAME,KEY_USERNAME,KEY_USER_PASSWORD,KEY_USER_DOB,KEY_USER_AGE,KEY_USER_HOBBIES,KEY_USER_GENDER,KEY_USER_IMG);
    public static final String SELECT_ALL_USER ="SELECT * FROM " + TABLE_NAME;
    public static final String GET_USER_BY_ID = String.format("SELECT * FROM %s WHERE %s =?",TABLE_NAME,KEY_USER_ID);
    public static final String GET_USER_ID_BY_USERNAME = String.format("SELECT * FROM %s WHERE %s =?",TABLE_NAME,KEY_USERNAME);



// empty constructor required...
    public User_Model() {
    }
// user registration constructor user_id not required becasue we are doing user_id uniq and auto increment...
    public User_Model(@NonNull String name, String username, String user_password, String user_dob, String user_age, String user_hobbies, String user_gender, byte[] user_image) {
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
    public User_Model(int user_id, String name, String username,  String user_dob, String user_age, String user_hobbies, String user_gender, byte[] user_image) {
        this.user_id = user_id;
        this.name = name;
        this.username = username;
        User_dob = user_dob;
        User_age = user_age;
        User_hobbies = user_hobbies;
        User_gender = user_gender;
        User_image = user_image;
    }
// user login contrctor with username and password only
    public User_Model(String username, String user_password) {
        this.username = username;
        User_password = user_password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
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

    public byte[] getUser_image() {
        return User_image;
    }

    public void setUser_image(byte[] user_image) {
        User_image = user_image;
    }
}
