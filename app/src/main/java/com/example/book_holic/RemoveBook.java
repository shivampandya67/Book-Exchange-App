package com.example.book_holic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RemoveBook extends AppCompatActivity {
    String imageUrl,booktitle,bookDesc,bookCategory,bookFor,bookPrice,phone;
    ImageView _bookImage;
    Button _remove;
    TextView _bookTitle,_bookDesc,_bookCategory,_bookFor,_bookPrice;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    ArrayList<bookdata> list;
    RemoveAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_book);
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
        _remove = findViewById(R.id.remove);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = databaseReference.child("book");
        list = new ArrayList<>();
        myAdapter = new RemoveAdapter(this, list);

        // Glide.with(context).load(book.getImageUrl()).into(holder.imageView);
        Glide.with(this).asBitmap().load(imageUrl).into(_bookImage);

        _bookTitle.setText(booktitle);
        _bookDesc.setText(bookDesc);
        _bookCategory.setText(bookCategory);
        _bookFor.setText(bookFor);
        _bookPrice.setText(bookPrice);

        _remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String key = ds.getKey();
                            if (key.equals(phone)) {

                            DatabaseReference subKey = databaseReference1.child(key);

                            ValueEventListener childEventListener = subKey.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds1 : snapshot.getChildren()) {
                                        bookdata book = ds1.getValue(bookdata.class);
                                        if (subKey.equals(booktitle)) {
                                        }
                                        ds1.getRef().removeValue();
                                    }
                                    myAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

    }
}