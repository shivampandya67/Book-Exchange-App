package com.example.book_holic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class sidebarNewUser extends AppCompatActivity {
    NavigationView nav;
    View mHeaderView;

    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    //recyclerView
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    MyAdapter myAdapter,myAdapter1;
    ArrayList<bookdata> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar_new_user);
        //recycler view
        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = databaseReference.child("book");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new MyAdapter(list, this);

        recyclerView.setAdapter(myAdapter);
        //start fetching data
        //first iteration
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    DatabaseReference subKey = databaseReference1.child(key);
                    ValueEventListener childEventListener = subKey.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds1 : snapshot.getChildren()) {
                                bookdata book = ds1.getValue(bookdata.class);
                                list.add(book);
//                                Log.d(mi"key", String.valueOf(ds1.child("Title").getValue()));
                            }
                            int i = myAdapter.getItemCount();
                            Log.d("adaptercount", String.valueOf(i));
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        nav = findViewById(R.id.navmenu1);
        drawerLayout = findViewById(R.id.drawer1);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.three));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home1:
                        Intent home= new Intent(sidebarNewUser.this,sidebarNewUser.class);
                        startActivity(home);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about1:
                        Intent abt = new Intent(sidebarNewUser.this, about.class);
                        startActivity(abt);
                        //Toast.makeText(getApplicationContext(),"Help panel is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.login:
                        Intent inte = new Intent(sidebarNewUser.this, Login.class);
                        startActivity(inte);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setBackgroundColor(getResources().getColor(R.color.three));
//        ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
//        searchIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_search_icon));
       // ImageView searchIcon = menuItem.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //myAdapter.getFilter().filter(s.toLowerCase());
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?").setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sidebarNewUser.super.onBackPressed();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}