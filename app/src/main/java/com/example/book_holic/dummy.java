package com.example.book_holic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class dummy extends AppCompatActivity {

    TextView fullname,about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        fullname = findViewById(R.id.full_name);
        about = findViewById(R.id.about);

        /*SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String value = sharedPreferences.getString("value","");
        fullname.setText(value);
        about.setText(value);*/
//        String uname=getIntent().getStringExtra("email");
//        String uabout=getIntent().getStringExtra("email");
//        fullname.setText(uname);
//        about.setText(uabout);
//        Intent intent=new Intent(dummy.this,sidebar.class);
//        startActivity(intent);
    }
}