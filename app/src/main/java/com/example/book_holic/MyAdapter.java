package com.example.book_holic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {


    ArrayList<bookdata> list;
    ArrayList<bookdata> listFull;
    Context context;

    public MyAdapter(ArrayList<bookdata> list,Context context) {
        this.list = list;
        this.context = context;
       // listFull = new ArrayList<>(list);
        listFull =list;

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
                Intent intent = new Intent(context,contact.class);
                //intent.putExtra("imageurl", book.hashCode());
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return FilterUser;
    }
    private Filter FilterUser = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            //String searchText = charSequence.toString().toLowerCase();
            ArrayList<bookdata>tempList = new ArrayList<>();
            if(charSequence.toString().trim().isEmpty())
            {

                Intent intent1 = new Intent(context,sidebarNewUser.class);
                tempList.addAll(listFull);
                Log.d("tempList", String.valueOf(tempList.get(0)));
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent1);
            }
            else
            {
                for(bookdata item : listFull)
                {
                    if(item.getCategory().toString().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            item.getTitle().toString().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        tempList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values=tempList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, @NotNull FilterResults results) {
            list.clear();
            list.addAll((ArrayList<bookdata>)results.values);
            //list.addAll((Collection<? extends bookdata>) filterResults.values);
            notifyDataSetChanged();
        }
    };


}