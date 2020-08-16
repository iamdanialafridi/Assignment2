package com.example.assignment2;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.Classess.User_Model;
import com.example.assignment2.Holder.User_Holder;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private static final int CHOSE_IMG_REQ_CODE = 1;
    RecyclerView recyclerView;
    RadioGroup genderRadio;
    ImageView UserImg;
    TextView showName, showUserName, showAge, showDOB, showGender, showHobbies;
    AutoCompleteTextView hobbiesView;
    SharedPreferences sharedPreferences;
    //    com.example.assignment2.Db_Helper db_helper;
    EditText name, username, password, age, dob, loginPassword, loginUsername;
    Button btnCapture, btnLoginView, btnRegView, btnReg, btnLogin, btnLogout, btnsignout;
    LinearLayout loginLayout, regLayout, panelLayout, layoutRv;
    String hobbies, Gender, USERNAME;
    int UID;
    List<User_Model> user_modelList = new ArrayList<>();
    User_Holder user_holder;
    // firebase variables
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Uri imgURL, imgURL2;
    SimpleArcDialog mDialog;
    private StorageReference mstorageRef;
    private StorageTask mUploadTask;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        genderRadio = findViewById(R.id.gender_group);
        name = findViewById(R.id.Name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        recyclerView = findViewById(R.id.userRv);
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
        layoutRv = findViewById(R.id.layoutRv);
        panelLayout = findViewById(R.id.panelLayout);
        regLayout = findViewById(R.id.regLayout);
        btnLoginView = findViewById(R.id.btnLoginView);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogout = findViewById(R.id.btnLogout);
        btnsignout = findViewById(R.id.btnsignout);
        loginUsername = findViewById(R.id.LoginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        btnRegView = findViewById(R.id.btnRegView);
        btnReg = findViewById(R.id.btnReg);
        hobbiesView = findViewById(R.id.hobbies);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        mstorageRef = FirebaseStorage.getInstance().getReference("uploads");

        mDialog = new SimpleArcDialog(MainActivity.this);
        mDialog.setConfiguration(new ArcConfiguration(MainActivity.this));
        ArcConfiguration configuration = new ArcConfiguration(MainActivity.this);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(" Please wait...");
        mDialog.setCancelable(false);
        mDialog.setConfiguration(configuration);
        mDialog.setCancelable(false);
        sharedPreferences = getSharedPreferences("UserName", MODE_PRIVATE);

        String[] hobbiesString = getResources().getStringArray(R.array.hobbies);
        ArrayAdapter hobbieAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, hobbiesString);
        hobbiesView.setAdapter(hobbieAdapter);
        btnCapture = findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final CharSequence[] options = {"Choose from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, CHOSE_IMG_REQ_CODE);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });
        loginLayout.setVisibility(View.GONE);
        panelLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        layoutRv.setVisibility(View.GONE);

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
                } catch (Exception e) {
                    Log.d("error404", "" + e.getMessage());
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
                } catch (Exception e) {
                    Log.d("error404", "" + e.getMessage());
                }
            }
        });

        genderRadio.setOnCheckedChangeListener(this);
        if (firebaseAuth.getCurrentUser() != null) {
            mDialog.show();
            recyclerView.setVisibility(View.VISIBLE);
            layoutRv.setVisibility(View.VISIBLE);
            regLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.GONE);
            panelLayout.setVisibility(View.GONE);


            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User_Model userModel = dataSnapshot.getValue(User_Model.class);
                        user_modelList.add(userModel);
                    }
                    user_holder = new User_Holder(MainActivity.this, user_modelList);
                    recyclerView.setAdapter(user_holder);
                    mDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Database Connection Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            Intent intent = getIntent();
            if (intent.getStringExtra("USERID") == null) {
                recyclerView.setVisibility(View.VISIBLE);
                btnsignout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }
                });
            } else {
                String UID = intent.getStringExtra("USERID");
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("User").child(UID);
                mDialog.show();
                recyclerView.setVisibility(View.GONE);
                btnsignout.setVisibility(View.GONE);
                panelLayout.setVisibility(View.VISIBLE);
                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User_Model userModel = snapshot.getValue(User_Model.class);
                            Picasso.get().load(userModel.getUser_image()).fit()
                                    .placeholder(R.drawable.placeholder)
                                    .centerCrop().into(UserImg);
                            showName.setText(userModel.getName());
                            showUserName.setText(userModel.getUsername());
                            showGender.setText(userModel.getUser_gender());
                            showAge.setText(userModel.getUser_age());
                            showDOB.setText(userModel.getUser_dob());
                            showHobbies.setText(userModel.getUser_hobbies());
                            btnLogout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    firebaseAuth.signOut();
                                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                                }
                            });
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Database Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }


        }


    }


    private void User_Login_METHOD() {
        String EMAIL = loginUsername.getText().toString().trim();
        String PASSWORD = loginPassword.getText().toString().trim();
        if (TextUtils.isEmpty(EMAIL)) {
            loginUsername.setError("Required");
        } else if (TextUtils.isEmpty(PASSWORD)) {
            loginPassword.setError("Required");
        } else {
            mDialog.show();
            firebaseAuth.signInWithEmailAndPassword(EMAIL, PASSWORD)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            } else {
                                mDialog.dismiss();
                                loginLayout.setVisibility(View.GONE);
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                            }
                        }
                    });

        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.male:
                Gender = "Male";

                break;
            case R.id.female:
                Gender = "Female";
                break;
        }
    }

    private void User_Registration_method() {

        final String Name = name.getText().toString().trim();
        final String Email = username.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String DOB = dob.getText().toString().trim();
        final String Age = age.getText().toString().trim();
        hobbies = hobbiesView.getText().toString();
        if (TextUtils.isEmpty(Name)) {
            name.setError("Required");
        } else if (TextUtils.isEmpty(Email)) {
            username.setError("Required");
        } else if (TextUtils.isEmpty(Password)) {
            password.setError("Required");
        } else if (TextUtils.isEmpty(DOB)) {
            dob.setError("Required");
        } else if (TextUtils.isEmpty(Age)) {
            age.setError("Required");
        } else if (TextUtils.isEmpty(hobbies)) {
            hobbiesView.setError("Required");
        } else if (imgURL == null) {

            Toast.makeText(this, "Please Select User image", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(Gender)) {
            Toast.makeText(this, "Select User gender", Toast.LENGTH_SHORT).show();

        } else {
            mDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("registrationError1", "error\t" + task.getException().getMessage());

                    } else {
                        final StorageReference storageReference = mstorageRef.child(System.currentTimeMillis()
                                + "." + getFileExtension(imgURL));
                        storageReference.putFile(imgURL)
                                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            mDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                        return storageReference.getDownloadUrl();
                                    }
                                })
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {

                                            Uri downloadUri = task.getResult();

                                            String UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                            Log.d("imgurl", "" + downloadUri.toString());
                                            User_Model userModel = new User_Model(UID, Name, Email, Password, DOB, Age, hobbies, Gender, downloadUri.toString());
                                            databaseReference.child(UID).setValue(userModel);
                                            mDialog.dismiss();
                                            regLayout.setVisibility(View.GONE);
                                            loginLayout.setVisibility(View.VISIBLE);
                                        } else {
                                            mDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }

                }
            });


        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOSE_IMG_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imgURL = data.getData();
            Picasso.get().load(imgURL);
            Log.d("imgselected", "" + imgURL);
            Toast.makeText(this, "Image Selected From Gallery", Toast.LENGTH_SHORT).show();
        }


    }


}

