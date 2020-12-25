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

    public CategoryAdapter(ArrayList<String> cNames, ArrayList<Integer> cImages) {
        arrayList = cNames;
        imagelist = cImages;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.NameTv.setText(arrayList.get(position));
        holder.iconsIv.setImageResource(imagelist.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView NameTv;
        ImageView iconsIv;


        public ViewHolder(View itemView) {
            super(itemView);
            NameTv = itemView.findViewById(R.id.category);
            iconsIv = itemView.findViewById(R.id.icons);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cat = arrayList.get(getAdapterPosition());
                    Intent i = new Intent(view.getContext(), ShopFilter.class);
                    i.putExtra("category", cat);
                    view.getContext().startActivity(i);

                }
            });


        }
    }

    }



