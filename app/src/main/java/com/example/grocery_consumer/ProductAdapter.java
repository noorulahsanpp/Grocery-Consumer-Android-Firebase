package com.example.grocery_consumer;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductAdapter.ProductHolder> {
   static   FirebaseFirestore firebaseFirestore;
   static CollectionReference collectionReference ;
    static ArrayList<String> name = new ArrayList<>();
    static  ArrayList<String> num = new ArrayList<>();
    static ArrayList<String> images = new ArrayList<>();
     static ArrayList<String> imageurl = new ArrayList<>();
    static ArrayList<String> prices = new ArrayList<>();
    static  String storeid,userId,cartstoreid = "",cartid="";
    Integer quantity;
    static  Integer flag1 =0;
    static Date date = setDate();
    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options, String cartstoreid, String storeid, String userId) {
        super(options);

        this.storeid = storeid;
        this.userId = userId;
        this.cartstoreid = cartstoreid;
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("customers").document(userId).collection("cart");
        if(!cartstoreid.equals(storeid)){
            name.clear();
            num.clear();                        //clears array if store not present in cart
            images.clear();
            prices.clear();
            imageurl.clear();
               }
        getCartProducts();      //to call the cart products if present
    }

        public static void setProducts() {
        FirebaseFirestore firebaseFirestore;
        firebaseFirestore = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference ;
        collectionReference = firebaseFirestore.collection("customers").document(userId).collection("cart");
        if(!name.isEmpty()){

            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> products = new HashMap<>();
                        products.put("name", name);
                        products.put("itemno", num);
                        products.put("image", images);
                        products.put("price", prices);
                        products.put("date", date);
                        products.put("storeid",storeid);
                        products.put("status", "added to cart");
                        collectionReference.document("cart").set(products);
                    }
                    getCartProducts();
                }

            });

        }
        else {

            deletecart();
        }
    }


public static void deletecart(){

    collectionReference.document("cart")
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    cartid="";
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
}

    public static Date setDate(){
        Calendar start = Calendar.getInstance();
        start.setTime(new Date());
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        Date today = start.getTime();
        return today;
    }
    public static void getCartProducts(){
               collectionReference.whereEqualTo("storeid",storeid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        name = (ArrayList<String>) document.get("name");
                        num = (ArrayList<String>) document.get("itemno");
                        prices = (ArrayList<String>) document.get("price");
                        images = (ArrayList<String>) document.get("image");
                        cartid = document.get("storeid").toString();
                    }
                }

            }


        });

    }
    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, final int position, @NonNull final Product products) {
        holder.price.setText(products.getPrice().toString());
        quantity = products.getQuantity();
        holder.topic.setText(products.getName());
        String topic = products.getName();
        String imageUrl = products.getImage();
        imageurl.add(imageUrl);
        Picasso.get().load(imageUrl).into(holder.image);
        holder.edit.setRange(0,quantity);
        if (num.size()!= 0) {
            for (int i = 0; i < num.size(); i++) {
                if (topic.equals(name.get(i))) {
                    String itemnumber = num.get(i);
                    holder.edit.setNumber(itemnumber);
                    break;
                }
                else{
                   holder.edit.setNumber("0");
                }
            }
                    if(holder.edit.getNumber().equals("0")) {
                        holder.add.setEnabled(true);
                        holder.edit.setEnabled(false);
                    }
                    else{
                        holder.add.setEnabled(false);
                        holder.edit.setEnabled(true);
                    }
            }

        else{
            holder.edit.setNumber("0");
            holder.edit.setEnabled(false);
            holder.add.setEnabled(true);

        }
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_recyclerview, parent, false);

        return new ProductHolder(view);
    }


    class ProductHolder extends RecyclerView.ViewHolder {
        TextView topic;
        TextView price;
        ImageView image;
        Button add;
        String value;
        ElegantNumberButton edit;
        String n, i, p;
        int flag = 0;

        public ProductHolder(View itemView) {
            super(itemView);

            topic = itemView.findViewById(R.id.itemname);
            price = itemView.findViewById(R.id.itemprice);
            image = itemView.findViewById(R.id.itemimage);
            add = itemView.findViewById(R.id.itemadd);
            edit = itemView.findViewById(R.id.editbutton);



            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!cartid.equals("")){// value in cart
                        flag1 = 1;
                    }
                    if(flag1 == 1){
                        if (storeid.equals(cartid)) {
                           setnewitems();
                        } else {
                            AskOption();
                        }}
                    else{
                       setnewitems();
                    }

                }
            });

            edit.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                    value = String.valueOf(newValue);
                    p = (String) price.getText();
                    i = imageurl.get(getAdapterPosition());
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
                        add.setEnabled(true);
                        edit.setEnabled(false);
                        for (int i = 0; i < num.size(); i++) {
                            String n1 = "0";
                            if (n1.equals(num.get(i))) {
                                num.remove(i);
                                name.remove(i);
                                prices.remove(i);
                                images.remove(i);
                                break;
                            }
                        }
                    }

                }


            });

            ProductList.addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setProducts();
                    if (name.isEmpty()) {
                        Toast.makeText(view.getContext(), "Please add items", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(view.getContext(), "Items added to cart", Toast.LENGTH_SHORT).show();

                    }
                }

            });

        }
        public void setnewitems(){
            add.setEnabled(false);
            edit.setEnabled(true);
            edit.setNumber("1");
            value = edit.getNumber();
            n = (String) topic.getText();
            p = (String) price.getText();
            i = imageurl.get(getAdapterPosition());
            name.add(n);
            num.add(value);
            prices.add(p);
            images.add(i);
        }
        public void AskOption() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemView.getContext());
            alertDialog.setMessage("Do you want to clear your cart and add new items ");
            alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cartid = storeid;
                  setnewitems();
                  flag1 =0;
                    deletecart();
                }
            });

            AlertDialog dialog = alertDialog.create();
            dialog.show();
        }

    }


    }




