package com.example.grocery_consumer;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.content.Intent;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
;
import java.util.List;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ShopAdapter extends FirestoreRecyclerAdapter<Shops, ShopAdapter.ShopHolder>{

    static List<String> shopid= new ArrayList<>();
    public static ArrayList<String> name = new ArrayList<>();

    public ShopAdapter(@NonNull FirestoreRecyclerOptions<Shops> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShopHolder holder,final int position, @NonNull final Shops shop) {
        String sid = shop.getUserid();
        shopid.add(sid);
        holder.shnameTv.setText(shop.getStorename());
        holder.shcategoryTv.setText(shop.getCategory());
        holder.shplaceTv.setText(shop.getlocation());
        String imageUrl =shop.getStoreimage();
        Picasso.get().load(imageUrl).into(holder.shimageIv);
    }

    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shops_recyclerview, parent, false);

        return new ShopHolder(view);
    }


    class ShopHolder extends RecyclerView.ViewHolder {
        TextView shnameTv;
        TextView shplaceTv;
        ImageView shimageIv;
        TextView shcategoryTv;


        public ShopHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View view) {
                    Intent i = new Intent(view.getContext(), ProductList.class);
                    i.putExtra("storeid", shopid.get(getAdapterPosition()));
                    view.getContext().startActivity(i);
                }
            });


            shnameTv = itemView.findViewById(R.id.shopsname);
            shplaceTv = itemView.findViewById(R.id.shopplace);
            shimageIv = itemView.findViewById(R.id.shopimage);
            shcategoryTv = itemView.findViewById(R.id.shopcategory);

        }
    }
}

