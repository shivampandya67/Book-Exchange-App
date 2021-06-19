package com.example.book_holic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class RemoveAdapter extends RecyclerView.Adapter<RemoveAdapter.MyViewHolder> {

    Context context;
    ArrayList<bookdata> list;

    public RemoveAdapter(Context context, ArrayList<bookdata> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        bookdata book=list.get(position);
//        Glide.with(context).load(list.get(position).getImageUrl()).into(holder.imageView);
        Glide.with(context).load(book.getImageUrl()).into(holder.imageView);

        holder.title.setText(book.getTitle());
        holder.desc.setText(book.getDescription());
        holder.category.setText(book.getCategory());
        holder.for_s_e.setText(book.getType());
        holder.price.setText(book.getAmount());
        holder.phone.setText(book.getphone());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RemoveBook.class);
                intent.putExtra("imageurl", book.getImageUrl());
                intent.putExtra("title", book.getTitle());
                intent.putExtra("description", book.getDescription());
                intent.putExtra("category", book.getCategory());
                intent.putExtra("for", book.getType());
                intent.putExtra("price", book.getAmount());
                intent.putExtra("phone", book.getphone());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,desc,category,for_s_e,price,phone,phoneTitle,descTitle;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneTitle=itemView.findViewById(R.id.lblPhone);
            descTitle=itemView.findViewById(R.id.lblDesc);

            imageView = itemView.findViewById(R.id.bookLogo);
            title=itemView.findViewById(R.id.title1);
            desc=itemView.findViewById(R.id.desc1);
            category=itemView.findViewById(R.id.for1);
            for_s_e=itemView.findViewById(R.id.category1);
            price=itemView.findViewById(R.id.price1);
            phone=itemView.findViewById(R.id.Phone1);

            phone.setVisibility(View.GONE);
            phoneTitle.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
            descTitle.setVisibility(View.GONE);



        }
    }
}