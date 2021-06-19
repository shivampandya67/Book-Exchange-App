package com.example.book_holic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {
    TextInputLayout full_name,email,password;
    String phoneNo;
    Button update;
    String _fullname,_email,_password,_phoneno;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");
        update = findViewById(R.id.update);
        full_name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        showAllUserData();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(v);
            }
        });
    }
    private void showAllUserData()
    {
        Intent intent = getIntent();
        _fullname = intent.getStringExtra("name");
        _email = intent.getStringExtra("email");
        _password = intent.getStringExtra("password");
        _phoneno = intent.getStringExtra("phone");

        full_name.getEditText().setText(_fullname);
        email.getEditText().setText(_email);
        password.getEditText().setText(_password);
    }

    public void update(View view){
        if(isPasswordChanged() | isNameChanged() | isEmailChanged())
        {
            Toast.makeText(this,"Data has been updated",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Data is same and can not be updated ",Toast.LENGTH_LONG).show();

        }
    }



    private boolean isPasswordChanged() {
        if(!_password.equals(password.getEditText().getText().toString()))
        {
            reference.child(_phoneno).child("password").setValue(password.getEditText().getText().toString());
            _password = password.getEditText().getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isNameChanged()
    {
            if(!_fullname.equals(full_name.getEditText().getText().toString()))
            {
                reference.child(_phoneno).child("name").setValue(full_name.getEditText().getText().toString());
                _fullname = full_name.getEditText().getText().toString();
                return true;
            }
            else
            {
                return false;
            }
    }
    private boolean isEmailChanged() {
        if(!_email.equals(email.getEditText().getText().toString()))
        {
            reference.child(_phoneno).child("email").setValue(email.getEditText().getText().toString());
            _email = email.getEditText().getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
}