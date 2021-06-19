package com.example.book_holic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNo extends AppCompatActivity {
    String verificationCodeBySystem;
    Button verify_btn;
    EditText phoneNoEnterdByTheUser;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseDatabase rootNode;
    String name,username,email,phoneNo,password;
    public int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);
        verify_btn = findViewById(R.id.verify_btn);
        phoneNoEnterdByTheUser = findViewById(R.id.verification_code_entered_by_user);
        progressBar = findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();
        flag=Integer.parseInt(getIntent().getExtras().get("flag").toString());
        phoneNo = getIntent().getStringExtra("phoneNo");


        if(flag==0) {
            password = getIntent().getStringExtra("password");
            name = getIntent().getStringExtra("name");
            username = getIntent().getStringExtra("username");
            email = getIntent().getStringExtra("email");
        }
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        sendVerificationCodeToUser(phoneNo);
        progressBar.setVisibility(View.GONE);
        verify_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String code = phoneNoEnterdByTheUser.getText().toString();
                if(code.isEmpty() || code.length()<6)
                {
                    phoneNoEnterdByTheUser.setError("wrong otp");
                    phoneNoEnterdByTheUser.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    //oncreate ends

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    //mCallbacks starts
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneNo.this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    };
    //mCallbacks ends


    private void verifyCode(String codeByUser){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
            signInTheUserByCredential(credential);
    }



        private void signInTheUserByCredential (PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(VerifyPhoneNo.this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (flag==0)
                {
                        if (task.isSuccessful()) {
                          UserHelper helperClass = new UserHelper(name, username, email, phoneNo, password);
                          reference.child(phoneNo).setValue(helperClass);
                          Intent intent = new Intent(getApplicationContext(), Login.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                        }
                        else {
                          Toast.makeText(VerifyPhoneNo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                }
                else
                {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), forgetPassword.class);
                        intent.putExtra("phoneNo",phoneNo);
                        //intent.putExtra("password",password);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(VerifyPhoneNo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



}