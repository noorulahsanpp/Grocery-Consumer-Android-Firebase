package com.example.grocery_consumer;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCart extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String TAG = "MyCart";
    SharedPreferences sharedPreferences;
    private CollectionReference collectionReference;
    private RecyclerView recyclerView;
    RelativeLayout itemlayout;
    private String shname,description,orderid;
    private Button placeorder;
    static String storeid ="",userId,username,phone;
    String image;
     ArrayList<String> images = new ArrayList<>();
     ArrayList<String> prices = new ArrayList<>();
   ArrayList<String> name = new ArrayList<>();
   ArrayList<String> itemno = new ArrayList<>();
   FirebaseFirestore firebaseFirestore;
    private ImageView shopimage;
    public static TextView shopname,details,cartvalue,total,method,discount,cartempty;
    static Date date = setDate();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        getSupportActionBar().hide();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getSharedPreference();
        recyclerView = findViewById(R.id.cartview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        initwidgets();
        cartempty.setVisibility(View.INVISIBLE);
        itemlayout.setVisibility(View.INVISIBLE);
        getCartProducts();
        ProductAdapter.imageurl.clear();


        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOldcart();
                collectionReference = firebaseFirestore.collection("customers").document(userId).collection("cart");
                collectionReference.document("cart")
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                Toast.makeText(getApplicationContext(), "Your Order is placed.", Toast.LENGTH_LONG).show();
                ProductAdapter.num.clear();
                ProductAdapter.name.clear();
                ProductAdapter.prices.clear();
                ProductAdapter.images.clear();
                finish();
                    }

        });

    }
    public void setOldcart(){
        collectionReference = firebaseFirestore.collection("customers").document(userId).collection("oldcart");
        Map<String, Object> products = new HashMap<>();
        products.put("name", name);
        products.put("itemno", itemno);
        products.put("orderid",orderid);
        products.put("date", date);
        products.put("image", images);
        products.put("customer",username);
        products.put("phone",phone);
        products.put("price", prices);
        products.put("status", "order placed");
        collectionReference.document().set(products);

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

    private void initwidgets(){
        shopname = findViewById(R.id.shopname);
        details = findViewById(R.id.details);
        cartvalue = findViewById(R.id.cartvalue);
        discount = findViewById(R.id.discount);
        total = findViewById(R.id.total);
        method = findViewById(R.id.payment);
        shopimage = findViewById(R.id.shopimage);
        placeorder = findViewById(R.id.placeorder);
        itemlayout = findViewById(R.id.itemlayout);
        cartempty = findViewById(R.id.cartempty);


    }
    private void getCartProducts(){

               collectionReference = firebaseFirestore.collection("customers").document(userId).collection("cart");
        collectionReference.document("cart").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        orderid = document.getId();
                        name = (ArrayList<String>) document.get("name");
                        storeid = document.get("storeid").toString();   //first time to fetch when no values in cart
                        itemno = (ArrayList<String>) document.get("itemno");
                        prices = (ArrayList<String>) document.get("price");
                        images = (ArrayList<String>) document.get("image");
                        itemlayout.setVisibility(View.VISIBLE);
                        cartempty.setVisibility(View.INVISIBLE);
                        setStorename(storeid);
                    } else {
                        Toast.makeText(getApplicationContext(), "Cart Empty.", Toast.LENGTH_LONG).show();
                        cartempty.setVisibility(View.VISIBLE);
                    }
                    CartAdapter adapter = new CartAdapter(userId,storeid,itemno,name, prices, images);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Cart Empty.", Toast.LENGTH_LONG).show();
                    cartempty.setVisibility(View.VISIBLE);
                }

                }


        });


           }

    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
        storeid = sharedPreferences.getString("storeid","");
        username = sharedPreferences.getString("username", "");
        phone = sharedPreferences.getString("phone", "");
    }


    @Override
    public void onStart() {
        super.onStart();
       // adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
  //     adapter.stopListening();
    }



    private void setStorename(String storeid){
        firebaseFirestore.collection("stores").document(storeid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document =task.getResult();
                    shname = (String) document.get("storename");
                    description = document.get("category").toString()+("\n")+document.get("location").toString()+("\n")+document.get("phone").toString();
                    image = document.get("storeimage").toString();
                    Picasso.get().load(image).into(shopimage);
                    shopname.setText(shname);
                    details.setText(description);


                }
            }
        });
    }


}