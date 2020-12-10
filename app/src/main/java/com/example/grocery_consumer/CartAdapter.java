package com.example.grocery_consumer;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedPreferences;
    ArrayList<String> prices = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
      ArrayList<String> imageurl = new ArrayList<>();
    float cartvalue = 0;
    ArrayList<String> num = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    private CartAdapter adapter;
    public CartAdapter(ArrayList<String> itemno1,ArrayList<String> name1, ArrayList<String> prices1, ArrayList<String> images1) {
        name = name1;
        prices = prices1;
        num = itemno1;
        images = images1;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_items, parent, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        holder.topic.setText(name.get(position));
        holder.price.setText(prices.get(position));
        String amount = prices.get(position);
        String imageUrl = images.get(position);
        imageurl.add(imageUrl);
        Picasso.get().load(imageUrl).into(holder.image);
        holder.topic.setText(name.get(position));
        String itemnum = num.get(position);
              holder.edit.setNumber(itemnum);

        cartvalue = cartvalue +( Float.parseFloat(amount)* Integer.parseInt(itemnum));
                      MyCart.cartvalue.setText("₹ "+cartvalue);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }


    public class CartHolder extends RecyclerView.ViewHolder {

        TextView topic;
        TextView price, discount;
        ImageView image;
        String value;
        ElegantNumberButton edit;
        String n, p, i;
        int flag = 0;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.itemname);
            price = itemView.findViewById(R.id.itemprice);
            image = itemView.findViewById(R.id.itemimage);
            edit = itemView.findViewById(R.id.editbutton);
            edit.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    p = (String) price.getText();
                    i = imageurl.get(getAdapterPosition());
                    value = String.valueOf(newValue);
                    n = (String) topic.getText();
                    if (name.size() == 0) {
                        name.add(n);
                        num.add(value);
                        prices.add(p);
                        images.add(i);
                    } else {
                        for (int i = 0; i < name.size(); i++) {
                            flag = 0;
                            if (n.equals(name.get(i))) {
                                num.set(i, value);
                                flag = 1;
                                break;
                            }
                        }
                        if (flag != 1) {
                            name.add(n);
                            num.add(value);
                            prices.add(p);
                            images.add(i);
                        }

                    }

                    if (newValue == 0) {
                //        edit.setEnabled(false);
                        for (int i = 0; i < num.size(); i++) {
                            String n1 = "0";
                            if (n1.equals(num.get(i))) {

                                num.remove(i);
                                name.remove(i);
                                prices.remove(i);
                                images.remove(i);
                                removeAt(i);
                                break;

                            }
                        }
                    }
                    cartvalue=0;
                    for(int i=0;i<num.size();i++)

                    cartvalue = cartvalue +( Float.parseFloat(prices.get(i))* Integer.parseInt(num.get(i)));
                    MyCart.cartvalue.setText("₹ "+cartvalue);
                                      ProductAdapter.num = num;
                    ProductAdapter.name = name;
                    ProductAdapter.prices = prices;
                    ProductAdapter.images = images;
                   ProductAdapter.setProducts();
MainActivity.setupBadge();
                }
            });

        }

    }
    public void removeAt(int position) {
          notifyItemRemoved(position);


    }
}

