package com.example.grocery_consumer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<String> arrayList;
    ArrayList<Integer> imagelist;

    String cat = "";

    public CategoryAdapter(ArrayList<String> mNames, ArrayList<Integer> mImages) {

        arrayList = mNames;
        imagelist = mImages;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.Name.setText(arrayList.get(position));
        holder.icons.setImageResource(imagelist.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView Name;
        ImageView icons;


        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cat = arrayList.get(getAdapterPosition());
                    Intent i = new Intent(view.getContext(), ShopFilter.class);
                    i.putExtra("category", cat);

                    view.getContext().startActivity(i);

                }
            });

            Name = itemView.findViewById(R.id.category);
            icons = itemView.findViewById(R.id.icons);
        }
    }

    }



