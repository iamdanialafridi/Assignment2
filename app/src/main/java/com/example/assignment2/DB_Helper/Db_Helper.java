package com.example.assignment2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import com.example.assignment2.Classess.User_Model;

import java.util.ArrayList;
import java.util.List;

public class Db_Helper extends SQLiteOpenHelper {

    public static final int FAILURE_CODE = -1;

    public static final String DB_NAME = "User";
    public static final int DB_VERSION = 1;
    private static Db_Helper instance;
    private Context context;
    public Db_Helper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public static Db_Helper getInstance(Context context) {
        if (instance == null) {
            instance = new Db_Helper(context);
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            db.execSQL(User_Model.CREATE_TBL);



            Log.d("db007", "created");
        } catch (Exception e) {
            Log.d("db007", "not" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + User_Model.TABLE_NAME);

            onCreate(db);
        }
    }

    public boolean Register_user(User_Model userModel) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.d("db007", "created");

        try {
            contentValues.put(User_Model.KEY_USER_NAME,userModel.getName());
            contentValues.put(User_Model.KEY_USERNAME,userModel.getUsername());
            contentValues.put(User_Model.KEY_USER_PASSWORD,userModel.getUser_password());
            contentValues.put(User_Model.KEY_USER_DOB,userModel.getUser_dob());
            contentValues.put(User_Model.KEY_USER_AGE,userModel.getUser_age());
            contentValues.put(User_Model.KEY_USER_GENDER,userModel.getUser_gender());
            contentValues.put(User_Model.KEY_USER_HOBBIES,userModel.getUser_hobbies());
            contentValues.put(User_Model.KEY_USER_IMG,userModel.getUser_image());




            long insertq = sqLiteDatabase.insertOrThrow(User_Model.TABLE_NAME, null, contentValues);
            if (insertq == FAILURE_CODE) return false;
            else
                return true;
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    public boolean checkUsernameIfexist(String username) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM USERS WHERE USERNAME=?",new String[]{username});
        if (cursor.getCount()>0) return  false;
        else return true;
    }

    public boolean User_Login(User_Model userModel) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?", new String[]{userModel.getUsername(), userModel.getUser_password()});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public User_Model getUID_BYUSERNAME(String username) {
        SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(User_Model.GET_USER_ID_BY_USERNAME, new String[]{username}, null);


        int UID;
        String name,Username,dob,age,gender,hobby;
        byte[] image;


        if (cursor != null && cursor.moveToFirst()) {

            name = cursor.getString(cursor.getColumnIndex(User_Model.KEY_USER_NAME));
            Username = cursor.getString(cursor.getColumnIndex(User_Model.KEY_USERNAME));
            dob = cursor.getString(cursor.getColumnIndex(User_Model.KEY_USER_DOB));
            age = cursor.getString(cursor.getColumnIndex(User_Model.KEY_USER_AGE));
            gender = cursor.getString(cursor.getColumnIndex(User_Model.KEY_USER_GENDER));
            hobby = cursor.getString(cursor.getColumnIndex(User_Model.KEY_USER_HOBBIES));
            image = cursor.getBlob(cursor.getColumnIndex(User_Model.KEY_USER_IMG));
            UID = cursor.getInt(cursor.getColumnIndex(User_Model.KEY_USER_ID));

            return new User_Model(UID,name,Username,dob,age,hobby,gender,image);


        } else {
            return null;
        }
    }

}

