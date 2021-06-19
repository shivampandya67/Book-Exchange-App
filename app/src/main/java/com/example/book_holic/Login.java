package com.example.book_holic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    Button callSignUp,btnlogin,forgetPassword;
    ImageView image;
    TextView logoText;
    TextInputLayout username,password;
    int flag = 1;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        logoText = findViewById(R.id.book);
        image = findViewById(R.id.imageView);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnlogin = findViewById(R.id.go);
        forgetPassword = findViewById(R.id.forget_password);
        callSignUp = findViewById(R.id.signup_screen);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String val = username.getEditText().getText().toString();
               String pass = password.getEditText().getText().toString();
                if (val.isEmpty())
                {
                    username.setError("please enter username");
                    username.requestFocus();
                    return;
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("phoneNo",val);
                    //intent.putExtra("password",pass);
                    startActivity(intent);
                }
            }
        });
        callSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Login.this,SignUp.class);
                Pair[] pairs = new Pair[5];

                pairs[0] = new Pair<View,String>(image,"logo_name");
                pairs[1] = new Pair<View,String>(logoText,"logo_text");
                pairs[2] = new Pair<View,String>(username,"username");
                pairs[3] = new Pair<View,String>(password,"password");
                pairs[4] = new Pair<View,String>(btnlogin,"btn_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);

                startActivity(intent,options.toBundle());
             //   finish();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUsername())
                {
                    return;
                }
                else if(!validatePassword())
                {
                    return;
                }
                else
                {
                    isUser();

                }

                /*Intent intent = new Intent(Login.this,sidebar.class);
                startActivity(intent);*/
            }
        });
    }

    private void isUser() {
        String userEnteredUsername = username.getEditText().getText().toString().trim();
        String userEnteredPassword = password.getEditText().getText().toString().trim();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("phone").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    //System.out.println(passwordFromDB);
                   // if(passwordFromDB.equals(userEnteredPassword))
                    if(userEnteredPassword.equals(passwordFromDB))
                    {
                        password.setError(null);
                        password.setErrorEnabled(false);
                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneNoFromDB = snapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);

                        /*SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("value", phoneNoFromDB);
                        editor.apply();*/

                        Intent intent = new Intent(getApplicationContext(),sidebar.class);
                        //Intent inte = new Intent(getApplicationContext(),dummy.class);
                        //inte.putExtra("email",emailFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("name",nameFromDB);
                        intent.putExtra("password",passwordFromDB);
                        intent.putExtra("phone",phoneNoFromDB);
                        intent.putExtra("username",usernameFromDB);
//                        intent.putExtra("Flag","1");
                        startActivity(intent);
                    }
                    else
                    {
                          password.setError("wrong password");
                          password.requestFocus();
                    }

                }
                else
                {
                    username.setError("no such user exist");
                    username.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private Boolean validateUsername()
    {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty())
        {
            username.setError("Field can not be empty");
            return false;
        }
        else
        {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword()
    {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty())
        {
            password.setError("Field can not be empty");
            return false;
        }
        else
        {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }


}