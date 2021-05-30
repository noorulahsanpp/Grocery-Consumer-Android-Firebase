package com.example.grocery_consumer;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class PreviousSubAdapter extends RecyclerView.Adapter<PreviousSubAdapter.SubItemViewHolder> {

    static ArrayList<String> image = new ArrayList<>();
    static ArrayList<String> arrprice = new ArrayList<>();
    static ArrayList<String> name = new ArrayList<>();
    static  ArrayList<String> num = new ArrayList<>();

    public PreviousSubAdapter(ArrayList<String> itemname, ArrayList<String> quantity, ArrayList<String> images, ArrayList<String> prices) {
        name = itemname;
        arrprice = prices;
        num = quantity;
        image = images;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder holder, int position) {

       holder.productname.setText(name.get(position)+ "\n\n" + "₹"+ arrprice.get(position) + "\n" + "Qty: " +num.get(position));
       String imageUrl = image.get(position);
       Picasso.get().load(imageUrl).into(holder.itemimage);

    }
    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return name.size();
    }

    static class SubItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemimage;
        TextView productname;
        SubItemViewHolder(View itemView) {
            super(itemView);
            itemimage = itemView.findViewById(R.id.itemimage);
            productname = itemView.findViewById(R.id.productname);
        }
    }
}