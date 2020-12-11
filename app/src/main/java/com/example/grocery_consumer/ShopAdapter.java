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

public class ShopAdapter extends FirestoreRecyclerAdapter<Stores, ShopAdapter.ShopHolder>{


    List<String> id= new ArrayList<>();
       public static ArrayList<String> name = new ArrayList<>();

        public ShopAdapter(@NonNull FirestoreRecyclerOptions<Stores> options) {
            super(options);

        }


    @Override
    protected void onBindViewHolder(@NonNull ShopHolder holder,final int position, @NonNull final Stores shop) {
        String uid = shop.getUserid();
        id.add(uid);
        holder.shname.setText(shop.getStorename());
        holder.shcategory.setText(shop.getCategory());
        holder.shplace.setText(shop.getlocation());
        String imageUrl =shop.getStoreimage();
        Picasso.get().load(imageUrl).into(holder.shimage);
    }



        @NonNull
        @Override
        public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shops_recyclerview, parent, false);

            return new ShopHolder(view);
        }


class ShopHolder extends RecyclerView.ViewHolder {
    TextView shname;
    TextView shplace;
    ImageView shimage;
    TextView shcategory;


    public ShopHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
              //  getCartProducts();
                Intent i = new Intent(view.getContext(), ProductList.class);
                i.putExtra("storeid", id.get(getAdapterPosition()));

                view.getContext().startActivity(i);
            }
        });


        shname = itemView.findViewById(R.id.shopsname);
        shplace = itemView.findViewById(R.id.shopplace);
        shimage = itemView.findViewById(R.id.shopimage);
        shcategory = itemView.findViewById(R.id.shopcategory);

    }
}
}

