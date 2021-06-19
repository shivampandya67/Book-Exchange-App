package com.example.book_holic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    TextInputLayout regName,regUsername,regEmail,regPhone,regPassword;
    Button regBtn, regToLogin;
    private  static  final String key="SignUp";
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        regName = findViewById(R.id.name);
        regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPhone = findViewById(R.id.phone);
        regPassword = findViewById(R.id.password);
        regBtn = findViewById(R.id.go);
        regToLogin = findViewById(R.id.goToLogin);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName())
                { return; }
                else if(!validateUserName())
                { return;}
                else if(!validateEmail())
                { return; }
                else if(!validatePhoneNo())
                {return;}
                else {
                    isUser();
                }
            }
        });
        regToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUp.this,Login.class);
                startActivity(intent);
            }
        });


    }


    //validation
    private Boolean validateName()
    {
        String val = regName.getEditText().getText().toString();
        if (val.isEmpty())
        {
            regName.setError("Field can not be empty");
            return false;
        }
        else
        {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUserName()
    {
        String val = regUsername.getEditText().getText().toString();
        String noWhitespace = "\\A\\w{4,20}\\z";
        if (val.isEmpty())
        {
            regUsername.setError("Field can not be empty");
            return false;
        }

        else
        {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail()
    {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty())
        {
            regEmail.setError("Field can not be empty");
            return false;
        }
        else if(!val.matches(emailPattern))
        {
            regEmail.setError("Invalid Email");
            return false;
        }
        else
        {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo()
    {
        String val = regPhone.getEditText().getText().toString();
        if (val.isEmpty())
        {
            regPhone.setError("Field can not be empty");
            return false;
        }
        else
        {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }


    private void isUser() {
        String userEnteredUsername = regPhone.getEditText().getText().toString().trim();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("phone").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                        regPhone.setError("user exist");
                    regPhone.requestFocus();
                }
                else
                {
                    regPhone.setError(null);
                    regPhone.setErrorEnabled(false);
//                    rootNode = FirebaseDatabase.getInstance();
//                    reference = rootNode.getReference("users");
                    String name = regName.getEditText().getText().toString();
                    String username = regUsername.getEditText().getText().toString();
                    String email = regEmail.getEditText().getText().toString();
                    String phoneNo = regPhone.getEditText().getText().toString();
                    String password = regPassword.getEditText().getText().toString();

                    Intent intent = new Intent(getApplicationContext(),VerifyPhoneNo.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("name",name);
                    intent.putExtra("username",username);
                    intent.putExtra("email",email);
                    intent.putExtra("phoneNo",phoneNo);
                    intent.putExtra("password",password);

                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // phone number verification
/*    public void verifyphonenum(String regPhone) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + regPhone, 60, TimeUnit.SECONDS, SignUp.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        signInUser(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d(key, "On varification failed" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String varificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(varificationId, forceResendingToken);
                        Log.d("key1",regPhone);
                        Dialog dialog = new Dialog(SignUp.this);
                        dialog.setContentView(R.layout.verify_popup);
                        EditText vc = dialog.findViewById(R.id.OTP);
                        Button vb = dialog.findViewById(R.id.otpCheck);
                        vb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String verificationCode = vc.getText().toString();
                                if (varificationId.isEmpty()) {
                                    return;
                                }
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(varificationId, verificationCode);
                                signInUser(credential);

                            }
                        });
                        dialog.show();
                    }

                });
    }
    private void signInUser(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignUp.this, Login.class));
                } else {
                    Log.d(key, "On complete" + task.getException().getLocalizedMessage());
                }
            }
        });

    }

    /*private Boolean validatePassword()
    {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])"+  //at least one digit
                "(?=.*[a-z])"+  //at least one lower case latter
                "(?=.*[A-Z])"+  //at least one upper case latter
                "(?=.*[a-zA-Z])"+ //any letters
                "(?=.*[@#$%^&+=])"+ //at least one special character
                "(?=\\s+$)"+// no white space
                ".{4,}"+// at least four character
                "$";
        if (val.isEmpty())
        {
            regPassword.setError("Field can not be empty");
            return false;
        }
        else if(!val.matches(passwordVal))
        {
            regPassword.setError("password is too weak");
            return false;
        }
        else
        {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }*/

    //save data in the firebase on button click
   /* public void registerUser(View view){
        if (!validateName() ) {
            return;
        }
        rootNode =FirebaseDatabase.getInstance();
        reference=rootNode.getReference("users");

        String name = regName.getEditText().getText().toString();
        String username = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNo = regPhone.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        UserHelper helperClass = new UserHelper(name,username,email,phoneNo,password);
        reference.child(phoneNo).setValue(helperClass);
    }*/
}