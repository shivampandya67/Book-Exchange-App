package com.example.book_holic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class sidebar extends AppCompatActivity {
    NavigationView nav;
    View mHeaderView;

    //private MenuItem menuItem1;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView fullname,about;

    //recycler view
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    MyAdapterLogin myAdapter;
    ArrayList<bookdata> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);

        //recycler  view
        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = databaseReference.child("book");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new MyAdapterLogin(list, this);
        recyclerView.setAdapter(myAdapter);

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    //Log.d("check",key);
                    DatabaseReference subKey = databaseReference1.child(key);
                    ValueEventListener childEventListener = subKey.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds1 : snapshot.getChildren()) {
                                bookdata book = ds1.getValue(bookdata.class);
                                list.add(book);
//                                Log.d("key", String.valueOf(ds1.child("Title").getValue()));
                            }
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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = findViewById(R.id.navmenu);
        mHeaderView = nav.getHeaderView(0);
        fullname =(TextView) mHeaderView.findViewById(R.id.full_name);
        about =(TextView) mHeaderView.findViewById(R.id.about);

        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String password = getIntent().getStringExtra("password");
        String name = getIntent().getStringExtra("name");

        about.setText(phone);

        drawerLayout=findViewById(R.id.drawer);
        toggle=new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.three));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menu_home :
                        Intent home= new Intent(sidebar.this,sidebar.class);
                        startActivity(home);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profile :
                        Intent intent= new Intent(sidebar.this,UserProfile.class);
                        intent.putExtra("phone",phone);
                        intent.putExtra("email",email);
                        intent.putExtra("name",name);
                        intent.putExtra("password",password);
                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(),"Profile panel is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.add_book :
                        Intent i= new Intent(sidebar.this,addBook.class);
                        i.putExtra("phone",phone);
                        startActivity(i);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.my_books :
                        Intent myBooks= new Intent(sidebar.this,MyBooks.class);
                        myBooks.putExtra("phone",phone);
                        startActivity(myBooks);
                        //Toast.makeText(getApplicationContext(),"My Books panel is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.about :
                        Intent abt=new Intent(sidebar.this,about.class);
                        startActivity(abt);
                        //Toast.makeText(getApplicationContext(),"Help panel is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.logout :
                        Intent inte= new Intent(sidebar.this,Login.class);
                        startActivity(inte);
                        Toast.makeText(getApplicationContext(),"Logout Successfully",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setBackgroundColor(getResources().getColor(R.color.three));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
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
        builder.setMessage("Logout!!..sure?").setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sidebar.super.onBackPressed();
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