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

    static FirebaseFirestore firebaseFirestore;
    static CollectionReference collectionReference ;
    static ArrayList<String> prdtname = new ArrayList<>();
    static ArrayList<String> prdtnum = new ArrayList<>();
    static ArrayList<String> prdtimages = new ArrayList<>();
    static ArrayList<String> prdtimageurl = new ArrayList<>();
    static ArrayList<String> prdtprices = new ArrayList<>();
    static String storeid,userId,cartstoreid;
    static Integer flag1 =0;
    static Date date = setDate();
    Integer quantity;


    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options, String storeid, String userId) {

        super(options);
        this.storeid = storeid;
        this.userId = userId;
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("customers").document(userId).collection("cart");
        // cart id is set on actionBar activity
        if(!cartstoreid.equals(storeid)){
            //clears array if the current store not present in cart
            prdtname.clear();
            prdtnum.clear();
            prdtimages.clear();
            prdtprices.clear();
            prdtimageurl.clear();
               }
        else
        {
            //to call the cart products if present
            getCartProducts();
        }
        try {
            //set time in mili
            Thread.sleep(700);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, final int position, @NonNull final Product products) {

        holder.priceTv.setText(products.getPrice().toString());
        quantity = products.getQuantity();
        holder.topicTv.setText(products.getName());
        String topic = products.getName();
        String imageUrl = products.getImage();
        prdtimageurl.add(imageUrl);
        Picasso.get().load(imageUrl).into(holder.imageIv);
        holder.editBtn.setRange(0,quantity);

        if (prdtnum.size()!= 0) {
            for (int i = 0; i < prdtnum.size(); i++) {
                if (topic.equals(prdtname.get(i))) {
                    String itemnumber = prdtnum.get(i);
                    holder.editBtn.setNumber(itemnumber);
                    break;
                }
                else{
                   holder.editBtn.setNumber("0");
                }
            }
                    if(holder.editBtn.getNumber().equals("0")) {
                        holder.addBtn.setEnabled(true);
                        holder.editBtn.setEnabled(false);
                    }
                    else{
                        holder.addBtn.setEnabled(false);
                        holder.editBtn.setEnabled(true);
                    }
            }

        else{
            holder.editBtn.setNumber("0");
            holder.editBtn.setEnabled(false);
            holder.addBtn.setEnabled(true);

        }
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_recyclerview, parent, false);
        return new ProductHolder(view);
    }


    class ProductHolder extends RecyclerView.ViewHolder {
        TextView topicTv;
        TextView priceTv;
        ImageView imageIv;
        Button addBtn;
        String value;
        ElegantNumberButton editBtn;
        String n, i, p;
        int flag = 0;

        public ProductHolder(View itemView) {
            super(itemView);

            topicTv = itemView.findViewById(R.id.itemname);
            priceTv = itemView.findViewById(R.id.itemprice);
            imageIv = itemView.findViewById(R.id.itemimage);
            addBtn = itemView.findViewById(R.id.itemadd);
            editBtn= itemView.findViewById(R.id.editbutton);


            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        if (!cartstoreid.equals("")) {// value in cart
                            flag1 = 1;
                        }
                        if (flag1 == 1) {
                            if (storeid.equals(cartstoreid)) {
                                setnewitems();
                            } else {
                                AskOption();
                            }
                        } else {
                            setnewitems();
                        }
                }
            });

            editBtn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                    value = String.valueOf(newValue);
                    p = (String) priceTv.getText();
                    i = prdtimageurl.get(getAdapterPosition());
                    n = (String) topicTv.getText();
                    if (prdtname.size() == 0) {
                        prdtname.add(n);
                        prdtnum.add(value);
                        prdtprices.add(p);
                        prdtimages.add(i);
                    } else {
                        for (int i = 0; i < prdtname.size(); i++) {
                            flag = 0;
                            if (n.equals(prdtname.get(i))) {
                                prdtnum.set(i, value);
                                flag = 1;
                                break;
                            }
                        }
                        if (flag != 1) {
                            prdtname.add(n);
                            prdtnum.add(value);
                            prdtprices.add(p);
                            prdtimages.add(i);
                        }
                    }

                    if (newValue == 0) {
                        addBtn.setEnabled(true);
                        editBtn.setEnabled(false);
                        for (int i = 0; i < prdtnum.size(); i++) {
                            String n1 = "0";
                            if (n1.equals(prdtnum.get(i))) {
                                prdtnum.remove(i);
                                prdtname.remove(i);
                                prdtprices.remove(i);
                                prdtimages.remove(i);
                                break;
                            }
                        }
                    }
                }


            });

            ProductList.addtocartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setProducts();
                    if (prdtname.isEmpty()) {
                        Toast.makeText(view.getContext(), "Please add items", Toast.LENGTH_SHORT).show();
                        ActionBarActivity.getquantity();
                    } else {
                        Toast.makeText(view.getContext(), "Items added to cart", Toast.LENGTH_SHORT).show();

                    }
                }

            });

        }

        public void setnewitems(){

            addBtn.setEnabled(false);
            editBtn.setEnabled(true);
            editBtn.setNumber("1");
            value = editBtn.getNumber();
            n = (String) topicTv.getText();
            p = (String) priceTv.getText();
            i = prdtimageurl.get(getAdapterPosition());
            prdtname.add(n);
            prdtnum.add(value);
            prdtprices.add(p);
            prdtimages.add(i);
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
                    cartstoreid = storeid;
                  setnewitems();
                    ActionBarActivity.getquantity();
                    deletecart();
                }
            });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
        }
    }

    public static void setProducts() {
        collectionReference = firebaseFirestore.collection("customers").document(userId).collection("cart");
        if(!prdtname.isEmpty()){

            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> products = new HashMap<>();
                        products.put("name", prdtname);
                        products.put("itemno", prdtnum);
                        products.put("image", prdtimages);
                        products.put("price", prdtprices);
                        products.put("date", date);
                        products.put("storeid",storeid);
                        products.put("status", "added to cart");
                        collectionReference.document("cart").set(products);
                    }
                    ActionBarActivity.getquantity();            //change badge when add to cart clicked
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
                      //  cartstoreid="";
                        flag1 =0;
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
        Date today = start.getTime();
        return today;
    }

    public static void getCartProducts(){

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        prdtname = (ArrayList<String>) document.get("name");
                        prdtnum = (ArrayList<String>) document.get("itemno");
                        prdtprices = (ArrayList<String>) document.get("price");
                        prdtimages = (ArrayList<String>) document.get("image");
                    }
                }
            }
        });
    }

}




