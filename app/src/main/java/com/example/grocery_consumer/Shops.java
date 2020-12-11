package com.example.grocery_consumer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shops extends MainActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    private ShopAdapter adapter;
    public static FirebaseFirestore firebaseFirestore;
    public static CollectionReference collectionReference;
  static   int flag = 0;
  String userID,username,phone;
    private FirebaseAuth mAuth;
    private ArrayList<Integer> mImages = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(Shops.this, UserLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;

        }
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        collectionReference = firebaseFirestore.collection("stores");
        setSharedPreferences();
     recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getCategory();

            getproducts();

    }

//    public void setSharedPreferences(){
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("storeid", storeid);
//
//        editor.commit();
//    }
//    public void getFilter(String categoryname){
//            FirebaseFirestore firebaseFirestore;
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        CollectionReference collectionReference;
//        collectionReference = firebaseFirestore.collection("stores");
//        Query query = collectionReference.whereEqualTo("category",categoryname+"");
//        FirestoreRecyclerOptions<Stores> options = new FirestoreRecyclerOptions.Builder<Stores>()
//                .setQuery(query, Stores.class)
//                .build();
//
//        adapter = new ShopAdapter(options);
//        recyclerView.setAdapter(adapter);
//
//    }
public void setSharedPreferences(){
    sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("userid", userID);
    editor.putString("username", username);
    editor.putString("phone", phone);
    editor.commit();
}
    private void getproducts() {

        Query query = collectionReference.orderBy("storename", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Stores> options = new FirestoreRecyclerOptions.Builder<Stores>()
                .setQuery(query, Stores.class)
                .build();

       adapter = new ShopAdapter(options);
        recyclerView.setAdapter(adapter);
//             setSharedPreferences();
    }

    private void getCategory(){

            mNames.add("General Store");
            mNames.add("Vegetables and Fruits");
            mNames.add("Electronics and Home Appliances");
            mNames.add("Groceries");
            mNames.add("Fish and Meat");
            mNames.add("Bakery");
            mNames.add("Restaurent");

        mImages.add(R.drawable.ic_action_store);
        mImages.add(R.drawable.ic_action_store);
        mImages.add(R.drawable.ic_action_electronics);
        mImages.add(R.drawable.ic_action_grocery);
        mImages.add(R.drawable.ic_action_store);
        mImages.add(R.drawable.ic_action_bakery);
        mImages.add(R.drawable.ic_action_restaurent);

        RecyclerView recyclerView1 = findViewById(R.id.category);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setHasFixedSize(true);
        CategoryAdapter categoryAdapter = new CategoryAdapter(mNames,mImages);
        recyclerView1.setAdapter(categoryAdapter);



    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}