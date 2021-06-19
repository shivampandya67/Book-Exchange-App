package com.example.book_holic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBooks extends AppCompatActivity {
    //recycler view
    String phone;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    RemoveAdapter myAdapter;
    ArrayList<bookdata> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);


        phone = getIntent().getStringExtra("phone");
        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = databaseReference.child("book");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new RemoveAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    if (key.equals(phone)) {

                    //Log.d("check",key);
                    DatabaseReference subKey = databaseReference1.child(key);
                    ValueEventListener childEventListener = subKey.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds1 : snapshot.getChildren()) {
                                bookdata book = ds1.getValue(bookdata.class);
                                list.add(book);
                            }
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
//                    else{
//                        Toast.makeText(getApplicationContext(),"book not found",Toast.LENGTH_SHORT).show();
//                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}