package com.example.grocery_consumer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {

    static ArrayList<String> image = new ArrayList<>();
    static ArrayList<String> price = new ArrayList<>();
    public static ArrayList<String> name = new ArrayList<>();
    public static  ArrayList<String> num = new ArrayList<>();


    public SubItemAdapter(ArrayList<String> itemname, ArrayList<String> quantity, ArrayList<String> images, ArrayList<String> prices) {
        name = itemname;
        price = prices;
        num = quantity;
        image = images;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder holder, int position) {
        holder.productname.setText(name.get(position));
        holder.price.setText(price.get(position));
        String imageUrl = image.get(position);
        Picasso.get().load(imageUrl).into(holder.itemimage);
        holder.quantity.setText(num.get(position));

    }

    @Override
    public int getItemCount() {
        return name.size();
    }


    class SubItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemimage;
        TextView productname,quantity,price;

        SubItemViewHolder(View itemView) {
            super(itemView);
            itemimage = itemView.findViewById(R.id.itemimage);
            productname = itemView.findViewById(R.id.productname);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.itemprice);
        }
    }
}