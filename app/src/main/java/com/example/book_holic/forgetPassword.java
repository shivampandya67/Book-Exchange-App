package com.example.book_holic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class forgetPassword extends AppCompatActivity {
    EditText newpassword;
    Button updatepassword;

    String phoneNo;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        reference = FirebaseDatabase.getInstance().getReference("users");
        newpassword = findViewById(R.id.newPassword);
        updatepassword = findViewById(R.id.updatePassword);

        phoneNo = getIntent().getStringExtra("phoneNo");
        //_password = getIntent().getStringExtra("password");

        updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(v);
            }
        });
    }
    public void update(View view){
        reference.child(phoneNo).child("password").setValue(newpassword.getText().toString());
        Intent intent = new Intent(forgetPassword.this,Login.class);
        startActivity(intent);
        finish();
    }
}