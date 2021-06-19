package com.example.book_holic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class contact extends AppCompatActivity {
    String imageUrl,booktitle,bookDesc,bookCategory,bookFor,bookPrice,phone;
    ImageView _bookImage;
    Button _contact;
    TextView _bookTitle,_bookDesc,_bookCategory,_bookFor,_bookPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //Integer imageUrl = getIntent().getIntExtra("imageurl",0);
        imageUrl = getIntent().getStringExtra("imageurl");
        booktitle = getIntent().getStringExtra("title");
        bookDesc = getIntent().getStringExtra("description");
        bookCategory = getIntent().getStringExtra("category");
        bookFor = getIntent().getStringExtra("for");
        bookPrice = getIntent().getStringExtra("price");
        phone = getIntent().getStringExtra("phone");


        _bookImage = findViewById(R.id.book_image);
        _bookTitle = findViewById(R.id.book_title);
        _bookDesc = findViewById(R.id.book_description);
        _bookCategory = findViewById(R.id.book_category);
        _bookFor = findViewById(R.id.book_for);
        _bookPrice = findViewById(R.id.book_price);
        _contact = findViewById(R.id.contact);

       // Glide.with(context).load(book.getImageUrl()).into(holder.imageView);
        Glide.with(this).asBitmap().load(imageUrl).into(_bookImage);

        _bookTitle.setText(booktitle);
        _bookDesc.setText(bookDesc);
        _bookCategory.setText(bookFor);
        _bookFor.setText(bookCategory);
        _bookPrice.setText(bookPrice);

        _contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+"+91"+phone));
                startActivity(intent);
            }
        });

    }
}