package com.example.assignment2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment2.Classess.User_Model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    static Bitmap img,img1;
SessionManager sessionManager;
ImageView UserImg;
TextView showName,showUserName,showAge,showDOB,showGender,showHobbies;
    RadioGroup genderRadio;
SharedPreferences sharedPreferences;
com.example.assignment2.Db_Helper db_helper;
    AutoCompleteTextView hobbiesView;
    EditText name,username,password,age,dob,loginPassword,loginUsername;
    Button btnCapture,btnLoginView,btnRegView,btnReg,btnLogin,btnLogout;
    LinearLayout loginLayout,regLayout,panelLayout;
    private static final int CHOSE_IMG_REQ_CODE=1;
    private static final int Capture_IMG=2;
    String hobbies,Gender,USERNAME;
    int UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        genderRadio = findViewById(R.id.gender_group);
        name = findViewById(R.id.Name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        age = findViewById(R.id.age);
        dob = findViewById(R.id.dob);
        UserImg = findViewById(R.id.UserImg);
        showHobbies = findViewById(R.id.showHobbies);
        showDOB = findViewById(R.id.showDOB);
        showAge = findViewById(R.id.showAge);
        showGender = findViewById(R.id.showGender);
        showName = findViewById(R.id.showName);
        showUserName = findViewById(R.id.showUsername);
        loginLayout = findViewById(R.id.loginLayout);
        panelLayout = findViewById(R.id.panelLayout);
        regLayout = findViewById(R.id.regLayout);
        btnLoginView = findViewById(R.id.btnLoginView);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogout = findViewById(R.id.btnLogout);
        loginUsername = findViewById(R.id.LoginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        btnRegView = findViewById(R.id.btnRegView);
        btnReg = findViewById(R.id.btnReg);
        hobbiesView=findViewById(R.id.hobbies);
        db_helper = com.example.assignment2.Db_Helper.getInstance(this);
        sessionManager = new SessionManager(this);
        sharedPreferences=getSharedPreferences("UserName",MODE_PRIVATE);

        String[] hobbiesString = getResources().getStringArray(R.array.hobbies);
        ArrayAdapter hobbieAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,hobbiesString);
        hobbiesView.setAdapter(hobbieAdapter);
        btnCapture = (Button)findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                if (intent.resolveActivity(getPackageManager())!=null){
//                    startActivityForResult(intent, CHOSE_IMG_REQ_CODE);
//                }
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo"))
                        {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, Capture_IMG);
                            }
                        }
                        else if (options[item].equals("Choose from Gallery"))
                        {
                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, CHOSE_IMG_REQ_CODE);
                        }
                        else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });
        loginLayout.setVisibility(View.GONE);
        panelLayout.setVisibility(View.GONE);

// login view here
        btnLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
            }
        });

        // login to user panel query here
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    User_Login_METHOD();
                } catch (Exception e){
                    Log.d("error404",""+e.getMessage());
                }
            }
        });


        //registration page view here
        btnRegView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginLayout.setVisibility(View.GONE);
                regLayout.setVisibility(View.VISIBLE);
            }
        });

        // registration starting here
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    User_Registration_method();
                } catch (Exception e){
                    Log.d("error404",""+e.getMessage());
                }
            }
        });
        genderRadio.setOnCheckedChangeListener(this);
        if (sessionManager.isLoggedIn()){
            regLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.GONE);
            panelLayout.setVisibility(View.VISIBLE);
            if (sharedPreferences.contains("USERNAME")){
                USERNAME = sharedPreferences.getString("USERNAME",USERNAME);
                User_Model userModel = db_helper.getUID_BYUSERNAME(USERNAME);
                UID = userModel.getUser_id();
                Bitmap bitmap = BitmapFactory.decodeByteArray(userModel.getUser_image(),0,userModel.getUser_image().length);
                //holder.img.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 380, 130, false));
                UserImg.setImageBitmap(bitmap);
                showName.setText(userModel.getName());
                showUserName.setText(userModel.getUsername());
                showGender.setText(userModel.getUser_gender());
                showAge.setText(userModel.getUser_age());
                showDOB.setText(userModel.getUser_dob());
                showHobbies.setText(userModel.getUser_hobbies());

            }
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sessionManager.logoutUser();
                }
            });

        }

    }

    private void User_Login_METHOD() {
        String USERNAME = loginUsername.getText().toString().trim();
        String PASSWORD = loginPassword.getText().toString().trim();
        if (TextUtils.isEmpty(USERNAME)){
            loginUsername.setError("Required");
        } else if (TextUtils.isEmpty(PASSWORD)){
            loginPassword.setError("Required");
        } else {
User_Model userModel = new User_Model(USERNAME,PASSWORD);
if (db_helper.User_Login(userModel)){
    sessionManager.createLoginSession(USERNAME,PASSWORD);
    @SuppressLint("CommitPrefEdits")
    SharedPreferences.Editor e=sharedPreferences.edit();
    e.putString("USERNAME",USERNAME);
    e.apply();

    startActivity(new Intent(MainActivity.this,MainActivity.class));

}
        }



    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.male:
                Gender = "Male";

                break;
            case R.id.female:
                Gender = "Female";
                break;
        }
    }
    private void User_Registration_method() {

        String Name = name.getText().toString().trim();
        String Username = username.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String DOB = dob.getText().toString().trim();
        String Age = age.getText().toString().trim();
        hobbies = hobbiesView.getText().toString();
        if (TextUtils.isEmpty(Name)){
            name.setError("Required");
        } else if (TextUtils.isEmpty(Username)){
            username.setError("Required");
        } else if (TextUtils.isEmpty(Password)){
            password.setError("Required");
        }else if (TextUtils.isEmpty(DOB)){
            dob.setError("Required");
        } else if (TextUtils.isEmpty(Age)){
            age.setError("Required");
        } else if (TextUtils.isEmpty(hobbies)) {
            hobbiesView.setError("Required");
        } else if (img == null) {

            Toast.makeText(this, "Please Select User image", Toast.LENGTH_SHORT).show();
        } if (TextUtils.isEmpty(Gender) ) {
            Toast.makeText(this, "Select User gender", Toast.LENGTH_SHORT).show();

        }else
         {
// insert data into user model contructor to create user object than add in database method to insert in db
            User_Model userModel = new User_Model(Name,Username,Password,DOB,Age,hobbies,Gender,dbimg(img));

            if (db_helper.checkUsernameIfexist(Username)) {
                if (db_helper.Register_user(userModel)) {
                    regLayout.setVisibility(View.GONE);
                    loginLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Username Already exist please use unique username", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int datasize = 0;
        if (requestCode == CHOSE_IMG_REQ_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                img = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                // pkgName.setText("Image Selected");

                File file = new File(String.valueOf(img));

                Toast.makeText(this, "User Image Selected", Toast.LENGTH_SHORT).show();
//                showimg.setImageBitmap(img);
                int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                if (file.length() > 1024) {
                    Toast.makeText(this, "filezie too large" + file_size, Toast.LENGTH_SHORT).show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }
        }


        if (requestCode == Capture_IMG && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();

            if (extras != null) {
                img = extras.getParcelable("data");
                // convert bitmap to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                showimg.setImageBitmap(img);

                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
                // Inserting Contacts
                Log.d("Insert: ", "Inserting ..");


            }
        }
    }
    public static byte[] dbimg(Bitmap img) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        img.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        byte Imgbyte[] = stream.toByteArray();
        return Imgbyte;
    }

}